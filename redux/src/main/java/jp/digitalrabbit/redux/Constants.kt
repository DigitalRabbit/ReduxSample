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