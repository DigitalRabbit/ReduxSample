package jp.digitalrabbit.redux

/**
 * Redux アーキテクチャ向け Store class.
 *
 * Redux アーキテクチャを実現するための Store を表現する class.
 *
 * @property reducer    Reducer object
 *
 * @constructor State の設定、 reducer の保管を行う。
 *
 * @param initialState  初期状態
 * @param reducer       この Store が使用する reducer
 */
class Store(initialState: State = EmptyState(), private val reducer: Reducer) {

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
    private val observers: MutableList<Observer> = mutableListOf()

    /**
     * Middleware 登録.
     *
     * この Store で使用する Middleware を登録する。
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
        var dispatcher = { act: Action -> dispatchImpl(act) }
        for (middleware in middlewares.reversed()) {
            dispatcher = middleware.executor(this, dispatcher)
        }
        dispatcher.invoke(action)
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
     * 空状態.
     *
     * constructor で初期化されなかった場合、これで初期化される。
     */
    class EmptyState : State
}
