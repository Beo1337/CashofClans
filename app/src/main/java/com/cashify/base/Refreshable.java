package com.cashify.base;

// Refreshable interface
// Declare something (preferably a fragment) refreshable. This is needed so that we can react to
// GUI events that need to recalculate things.

public interface Refreshable {
    void refresh();
}
