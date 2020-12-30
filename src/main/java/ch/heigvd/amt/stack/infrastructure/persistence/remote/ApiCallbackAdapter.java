package ch.heigvd.amt.stack.infrastructure.persistence.remote;

import ch.heigvd.gamify.ApiCallback;
import ch.heigvd.gamify.ApiException;
import java.util.List;
import java.util.Map;

public abstract class ApiCallbackAdapter<T> implements ApiCallback<T> {

  @Override
  public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {

  }

  @Override
  public void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders) {

  }

  @Override
  public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

  }

  @Override
  public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

  }
}
