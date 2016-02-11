package org.acm.sviet.schedulerama.interfaces;

/**
 * Created by Anurag on 11-02-2016.
 *
 * interface for callback service of refreshing an fragment due to some data changes.
 *
 * used in @link{CoursePlaceholderFragment, RepeatitionFragment} for refresh facility.
 */
public interface Refresh {
    public void callback();
}
