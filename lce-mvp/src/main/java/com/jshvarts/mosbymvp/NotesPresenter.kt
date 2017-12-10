package com.jshvarts.mosbymvp

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.jshvarts.mosbymvp.domain.GetNotesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NotesPresenter : MvpBasePresenter<NotesContract.View>(), NotesContract.Presenter {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun loadData(pullToRefresh: Boolean) {
        disposables.add(GetNotesUseCase.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.setData(it)
                    view?.showContent()
                }, { view?.showError(it, pullToRefresh) }))
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        Timber.d("In detachView " + retainInstance)
        if (!retainInstance) {
            Timber.d("Clearing disposables")
            disposables.clear()
        }
    }
}