package jp.digitalrabbit.reduxsample.ui

import jp.digitalrabbit.redux.Store
import jp.digitalrabbit.reduxsample.TextActionCreator

/**
 * 文字列更新用 Controller.
 *
 * layout ファイルに直接記載される事前提の文字列を操作する為の Controller object.
 */
object TextEditController {

    /**
     * 文字列更新.
     *
     * 文字列を更新する。
     *
     * @param str   更新文字列
     * @param store 更新先 Store object
     */
    @JvmStatic
    fun update(str: String, store: Store) {
        store.dispatch(TextActionCreator.updateString(str))
    }

    /**
     * 文字列削除.
     *
     * 文字列を削除する。
     */
    @JvmStatic
    fun delete(store: Store) {
        store.dispatch(TextActionCreator.deleteString())
    }
}