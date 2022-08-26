package nh.publy.backend.util;

import io.reactivex.rxjava3.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;

public class DemoPublisher {

  private static final Logger log = LoggerFactory.getLogger( DemoPublisher.class );

  private static DemoPublisher _instance;

  public synchronized static DemoPublisher getInstance() {
    if (_instance == null) {
      _instance = new DemoPublisher();
    }

    return _instance;
  }

  private Flowable<Integer> publisher;

  public DemoPublisher() {
    this.publisher = Flowable.create(new FlowableOnSubscribe<Integer>() {
      @Override
      public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
        int x = 0;
        while (x < Integer.MAX_VALUE) {
          int newValue = x++;
          log.info("Emitting {}", newValue);
          emitter.onNext(x);

          try {
            //noinspection BlockingMethodInNonBlockingContext
            Thread.sleep(200);
          } catch (Exception ex) {
            // egal
          }

          if (emitter.isCancelled()) {
            return;
          }
        }
      }
    }, BackpressureStrategy.BUFFER);
  }

  public Flowable<Integer> getPublisher() {
    return this.publisher;
  }
}
