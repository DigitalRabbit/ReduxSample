package jp.digitalrabbit.reduxsample

import jp.digitalrabbit.redux.Action

/**
 * 表示テキストに関する Action 向け sealed class.
 */
sealed class TextAction : Action {
    /**
     * 更新.
     *
     * 指定された文字列へと内容を更新する。
     *
     * @param payload 更新文字列
     */
    data class Update(val payload: String) : TextAction()

    /**
     * 削除.
     *
     * 文字列を削除する。
     */
    object Delete : TextAction()
}

/**
 * テキスト操作に関する Action を作成する Action creator.
 */
object TextActionCreator {
    /**
     * 文字列更新.
     *
     * 文字列を指定されたものへ更新する。
     *
     * @param str 指定文字列
     *
     * @return 文字列更新用 Action
     */
    fun updateString(str: String): Action = TextAction.Update(str)

    /**
     * 文字列削除.
     *
     * 現在の文字列を削除する。
     *
     * @return 文字列削除用 Action
     */
    fun deleteString(): Action = TextAction.Delete
}