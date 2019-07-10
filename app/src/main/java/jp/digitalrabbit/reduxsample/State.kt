package jp.digitalrabbit.reduxsample

import jp.digitalrabbit.redux.State

/**
 * このアプリケーションのメインの State.
 *
 * @property text このアプリケーション唯一のコンテンツ文字列
 */
data class MainState(
    val text: String = "uninitialized"
) : State