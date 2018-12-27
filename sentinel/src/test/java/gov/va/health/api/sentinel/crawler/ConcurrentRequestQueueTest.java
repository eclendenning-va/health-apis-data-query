package gov.va.health.api.sentinel.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ConcurrentRequestQueueTest {

  ConcurrentRequestQueue q = new ConcurrentRequestQueue();

  @Test(expected = IllegalStateException.class)
  public void exceptionIsThrownWhenAttemptingToGetNextFromEmptyQueue() {
    q.add("x");
    q.next();
    q.next();
  }

  @Test(expected = IllegalStateException.class)
  public void exceptionIsThrownWhenAttemptingToGetNextQueueThatWasNeverUsed() {
    q.next();
  }

  @Test
  public void hasNextReturnsFalseForEmptyQueue() {
    assertThat(q.hasNext()).isFalse();
    q.add("x");
    q.next();
    assertThat(q.hasNext()).isFalse();
  }

  @Test
  public void itemsAreRemovedFromQueueInOrderOfAddition() {
    q.add("a");
    q.add("b");
    q.add("c");
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("a");
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("b");
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("c");
    assertThat(q.hasNext()).isFalse();
  }

  @Test
  public void theSameItemCannotBeAddedToTheQueueTwice() {
    q.add("a");
    q.add("b");
    q.add("c");
    q.add("a"); // ignored
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("a");
    q.add("a"); // ignored
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("b");
    assertThat(q.hasNext()).isTrue();
    assertThat(q.next()).isEqualTo("c");
    assertThat(q.hasNext()).isFalse();
  }
}
