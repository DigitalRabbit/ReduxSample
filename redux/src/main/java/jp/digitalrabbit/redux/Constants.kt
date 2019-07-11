@file:Suppress("KDocUnresolvedReference")

package jp.digitalrabbit.redux

/**
 * Action class 向け interface.
 *
 * Action class として機能する事を宣言する為の interface.
 */
interface Action

/**
 * State class 向け interface.
 *
 * State class として機能する事を宣言する為の interface.
 */
interface State

/**
 * Reducer.
 *
 * Store で利用する Reducer 用 TypeAlias.
 *
 * @param action    処理内容
 * @param state     処理前状態
 *
 * @return 処理後状態
 */
typealias Reducer = (action: Action, state: State) -> State

/**
 * 購読用 TypeAlias.
 *
 * State の変更を購読するための TypeAlias.
 *
 * @param state 最新状態
 */
typealias Observer = (state: State) -> Unit

/**
 * Middleware 定義用 interface.
 *
 * Middleware として機能する事を宣言する為の interface.
 *
 * @note この interface をベースとして同期用/非同期用の middleware が用意される。
 */
interface Middleware

/**
 * 同期 Middleware 定義用 interface.
 *
 * 同期処理を行う Middleware function を表現する。
 */
interface SyncMiddleware : Middleware {
    /**
     * 同期処理実行.
     *
     * Middleware としての処理を同期実行する。
     *
     * @note 処理内で next を呼ばなかった場合、 reducer の処理もキャンセルされる。
     *
     * @param store     対象 Store
     * @param action    状態変更指示 Action
     * @param next      次の処理への Dispatcher
     */
    fun execute(store: Store, action: Action, next: (Action) -> Unit)
}

/**
 * 非同期 Middleware 定義用 interface.
 *
 * 非同期処理を行う Middleware function を表現する。
 */
interface AsyncMiddleware : Middleware {
    /**
     * 非同期処理実行.
     *
     * Middleware としての処理を非同期実行する。
     *
     * @note この method で何も行われなかった場合でも、次の処理へ移行する。
     *
     * @param store     対象 Store
     * @param action    状態変更指示 Action
     */
    suspend fun execute(store: Store, action: Action)
}
