package com.kodelabs.boilerplate.domain.interactors.impl;

import com.kodelabs.boilerplate.domain.executor.Executor;
import com.kodelabs.boilerplate.domain.executor.MainThread;
import com.kodelabs.boilerplate.domain.interactors.WelcomingInteractor;
import com.kodelabs.boilerplate.domain.interactors.base.AbstractInteractor;
import com.kodelabs.boilerplate.domain.repository.MessageRepository;

/**
 * This is an interactor boilerplate with a reference to a model repository.
 * <p/>
 */
public class WelcomingInteractorImpl extends AbstractInteractor implements WelcomingInteractor {

    private WelcomingInteractor.Callback mCallback;
    private MessageRepository            mMessageRepository;

    public WelcomingInteractorImpl(Executor threadExecutor,
                                   MainThread mainThread,
                                   Callback callback, MessageRepository messageRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mMessageRepository = messageRepository;
    }

    @Override
    public void run() {
        final String message = mMessageRepository.getWelcomeMessage();

        // check if we have failed to retrieve our message
        if (message == null || message.length() == 0) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onRetrievalFailed("Nothing to welcome you with :(");
                }
            });

            return;
        }

        // we have retrieved our message, notify the UI on the main thread
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMessageRetrieved(message);
            }
        });
    }
}
