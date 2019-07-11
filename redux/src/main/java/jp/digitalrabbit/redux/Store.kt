package jp.digitalrabbit.redux

import kotlinx.coroutines.*

/**
 * Redux アーキテクチャ向け Store class.
 *
 * Redux アーキテクチャを実現するための Store を表現する class.
 *
 * @property reducer    Reducer object
 * @property scope      この Store への dispatch に使用される coroutineScope (middleware の非同期処理にも使用)
 *
 * @constructor State の設定、 Reducer, CoroutineScope の保管を行う。
 *
 * @param initialState  初期状態
 * @param reducer       この Store が使用する Reducer
 * @param scope         非同期処理用 CoroutineScope
 */
class Store(
    initialState: State = EmptyState(),
    private val reducer: Reducer,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {

    /**
     * 空状態.
     *
     * constructor で初期化されなかった場合、これで初期化される。
     */
    class EmptyState : State

    /**
     * 状態.
     *
     * この Store が保持すべき状態。
     * 外部からは参照のみ可能。
     */
    var state: State = initialState
        private set(value) {
            field = value
        }

    /**
     * Middleware リスト.
     *
     * dispatch 内で処理される middleware のリスト。
     */
    private val middlewares = mutableListOf<Middleware>()

    /**
     * 購読済み observer リスト.
     *
     * 変更を観察する observer のリスト.
     */
    private val observers = mutableListOf<Observer>()

    /**
     * Middleware 登録.
     *
     * この Store で使用する Middleware を登録する。
     *
     * @param middleware 登録する middleware object
     */
    fun applyMiddleware(vararg middleware: Middleware) {
        middlewares.addAll(middleware)
    }

    /**
     * 購読.
     *
     * 状態の変更を購読する。
     *
     * @param observer 購読用 Observer function
     *
     * @return 購読解除用 function object
     */
    fun subscribe(observer: Observer): () -> Unit {
        observers.add(observer)
        return { observers.remove(observer) }
    }

    /**
     * Action 発送.
     *
     * Action を Middleware で処理し、実際の dispatch を行う。
     *
     * @param action    状態変更支持 Action
     */
    fun dispatch(action: Action) {
        scope.launch {
            var dispatcher = { act: Action -> dispatchImpl(act) }
            for (middleware in middlewares.reversed()) {
                dispatcher = generateFunc(middleware, this@Store, dispatcher)
            }
            dispatcher.invoke(action)
        }
    }

    /**
     * 処理停止.
     *
     * この Store を通して行われている処理を停止する。
     */
    fun cancel() {
        scope.coroutineContext.cancelChildren()
    }

    /**
     * Action 発送.
     *
     * Action を処理し、 state を変更する。
     * その後 subscriber へ変更を通知する。
     *
     * @param action    状態変更指示 Action
     */
    private fun dispatchImpl(action: Action) {
        val new = reducer(action, state)
        if (new !== state) {
            state = new
            observers.forEach { it(new) }
        }
    }

    /**
     * Middleware function 生成.
     *
     * 同期/非同期の処理シーケンスに沿った function を生成する。
     *
     * @param middleware    処理対象 middleware
     * @param store         対象 Store
     * @param next          次の処理への Dispatcher
     *
     * @return 実行可能 middleware function
     */
    private fun generateFunc(middleware: Middleware, store: Store, next: (Action) -> Unit): (Action) -> Unit {
        when (middleware) {
            is SyncMiddleware -> {
                return {
                    middleware.execute(store, it, next)
                }
            }
            is AsyncMiddleware -> {
                return {
                    scope.launch {
                        middleware.execute(store, it)
                    }
                    next(it)
                }
            }
            else -> return {}
        }
    }
}
