package br.com.lardopet.util

import io.reactivex.*

/**
 * Cria um Scheduler para a Reactive Pattern desejada utilizando as threads fornecidas
 * @param backgroundScheduler thread na qual o Observable seá inscrito
 * @param foregroundScheduler thread na qual o Observable será observado
 */
class SchedulerProvider(val backgroundScheduler: Scheduler, val foregroundScheduler: Scheduler) {

    /**
     * Cria um Scheduler para Observables
     * @return uma instãncia desse Scheduler
     */
    fun <T> getSchedulersForObservable(): (Observable<T>) -> Observable<T> {
        return { observable: Observable<T> ->
            observable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    /**
     * Cria um Scheduler para Singles
     * @return uma instãncia desse Scheduler
     */
    fun <T> getSchedulersForSingle(): (Single<T>) -> Single<T> {
        return { single: Single<T> ->
            single.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    /**
     * Cria um Scheduler para Completables
     * @return uma instãncia desse Scheduler
     */
    fun getSchedulersForCompletable(): (Completable) -> Completable {
        return { completable: Completable ->
            completable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    /**
     * Cria um Scheduler para Flowables
     * @return uma instãncia desse Scheduler
     */
    fun <T> getSchedulersForFlowable(): (Flowable<T>) -> Flowable<T> {
        return { flowable: Flowable<T> ->
            flowable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }
}