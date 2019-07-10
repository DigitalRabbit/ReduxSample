package jp.digitalrabbit.redux

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Redux アーキテクチャ向け Middleware 定義用 Abstract class.
 *
 * Middleware function を表現する。
 * Kotlin coroutines で非同期処理されることが前提となっている為にクラスとして定義され、
 * 中断/停止用の Job を保管する。
 *
 * @note scope が null の場合には同期処理、設定されている場合はその scope で実行される。
 *
 * @property scope 非同期処理実行用の CoroutineScope.
 *
 * @constructor CoroutineScope を受け取り property に設定する。
 *
 * @param scope  非同期処理実行用の CoroutineScope.
 */
abstract class Middleware(private val scope: CoroutineScope? = null) {

    /**
     * 同期処理.
     *
     * 同期的な middleware の処理として呼び出される。
     *
     * @note 処理内で next を呼ばなかった場合、 reducer の処理もキャンセルされる。
     *
     * @param store     対象 Store
     * @param action    状態変更指示 Action
     * @param next      次の処理への Dispatcher
     */
    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    open fun syncExecute(store: Store, action: Action, next: (Action) -> Unit) {
        // NOP
    }

    /**
     * 非同期処理.
     *
     * 非同期処理として reducer 実行前に呼び出される。
     *
     * @param store     対象 Store
     * @param action    状態変更指示 Action
     */
    @WorkerThread
    @Suppress("UNUSED_PARAMETER")
    open suspend fun execute(store: Store, action: Action) {
        // NOP
    }

    /**
     * Middleware 実行関数.
     *
     * この関数が呼ばれることでこれを継承する middleware の処理が実行される。
     */
    internal val executor: (Store, (Action) -> Unit) -> ((Action) -> Unit) = { store, next ->
        { action ->
            if (scope == null) {
                syncExecute(store, action, next)
            } else {
                scope.launch {
                    execute(store, action)
                }
                next(action)
            }
        }
    }
}
