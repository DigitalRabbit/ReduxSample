package jp.digitalrabbit.reduxsample

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.digitalrabbit.redux.Action
import jp.digitalrabbit.redux.State
import jp.digitalrabbit.redux.Store

/**
 * MainActivity 用 ViewModel
 */
class MainViewModel : ViewModel() {
    /**
     * この ViewModel を扱う画面用 Store object.
     */
    val store = Store(MainState(), ::reducer)

    /**
     * 画面表示用 State.
     */
    val data: MutableLiveData<MainState> = MutableLiveData((store.state as MainState).copy())

    /**
     * 状態変更時処理.
     *
     * Store 内で状態が変更された場合に UI へ変更を反映する為の Observer.
     *
     * @param state 変更後 State
     */
    @Suppress("KDocUnresolvedReference")
    private val observer = { state: State ->
        data.postValue((state as MainState).copy())
    }

    /**
     * 購読解除 function object.
     *
     *  Store への購読開始処理で返却される、解除用 function object.
     */
    private val unsubscribe = store.subscribe(observer)

    /**
     * EditText に入力された文字列保管用
     */
    @Bindable
    var editText: String = ""

    init {
        // Store へ Middleware を適用
        store.applyMiddleware(LogOutput(), DelayOutput())
    }

    /**
     * ViewModel 破棄処理.
     *
     * Store への購読解除および coroutine のキャンセルを行う。
     */
    override fun onCleared() {
        super.onCleared()

        unsubscribe()
        store.cancel()
    }
}

/**
 * この ViewModel が持つ Store の為の Top reducer function.
 *
 * @param action    処理内容
 * @param state     処理前状態
 *
 * @return 処理後状態
 */
fun reducer(action: Action, state: State): State {
    Log.d("reducer", "run: ${action::class.java.simpleName}")
    return when (action) {
        is TextAction.Update -> {
            MainState(action.payload)
        }
        is TextAction.Delete -> {
            MainState("")
        }
        else -> state
    }
}
