package jp.digitalrabbit.reduxsample

import android.util.Log
import jp.digitalrabbit.redux.Action
import jp.digitalrabbit.redux.Middleware
import jp.digitalrabbit.redux.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

/**
 * 同期 middleware サンプル.
 */
class SyncMiddleware : Middleware() {
    override fun syncExecute(store: Store, action: Action, next: (Action) -> Unit) {
        Log.d("SyncMiddleware", "syncExecute start.")
        next(action)
        Log.d("SyncMiddleware", "syncExecute end.")
    }
}

/**
 * 非同期 middleware サンプル.
 */
class AsyncMiddleware(scope: CoroutineScope) : Middleware(scope) {
    override suspend fun execute(store: Store, action: Action) {
        Log.d("AsyncMiddleware", "execute start.")
        delay(5000)     // 非同期確認の為の delay
        Log.d("AsyncMiddleware", "execute end.")
    }
}