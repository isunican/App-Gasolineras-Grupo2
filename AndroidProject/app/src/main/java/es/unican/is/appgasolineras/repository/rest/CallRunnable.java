package es.unican.is.appgasolineras.repository.rest;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * This class wraps the synchronous request of a Retrofit call in a Runnable
 * Synchronous calls in Retrofit cannot be performed in the main thread, so a background thread
 * must be launched.
 * This background thread will execute this runnable.
 * @param <T> the type of the response
 */
class CallRunnable<T> implements Runnable {
    private final Call<T> call;

    private T response = null;

    CallRunnable(Call<T> call) {
        this.call = call;
    }

    @Override
    public void run() {
        try {

            Response<T> callResponse = call.execute();
            T r = callResponse.body();
            setResponse(r);
        } catch (IOException e) {
            Log.i("control", "printStackTrace", e);
        }
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}