package jp.digitalrabbit.reduxsample

import android.util.Log
import jp.digitalrabbit.redux.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

/**
 * 同期 middleware サンプル.
 */
class LogOutput : SyncMiddleware {
    /**
     * 同期処理.
     *
     * Reducer の実行前でログを出力する。
     *
     * @param store     対象 Store
     * @param action    状態変更指示 Action
     * @param next      次の処理への Dispatcher
     */
    override fun execute(store: Store, action: Action, next: (Action) -> Unit) {
        Log.d("SyncMiddleware", "syncExecute start.")
        next(action)
        Log.d("SyncMiddleware", "syncExecute end.")
    }
}

/**
 * 非同期 middleware サンプル.
 */
class DelayOutput : AsyncMiddleware {
    /**
     * 非同期処理.
     *
     * 5秒間の待機を行い、待機前後にログを出力する。

     * @param store     対象 Store
     * @param action    状態変更指示 Action
     */
    override suspend fun execute(store: Store, action: Action) {
        Log.d("AsyncMiddleware", "execute start.")
        delay(5000)     // 非同期確認の為の delay
        Log.d("AsyncMiddleware", "execute end.")
    }
}