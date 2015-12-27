package profick.headache_forecast.service;

import profick.headache_forecast.data.Channel;

/**
 * Created by Denis on 28.12.2015.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
