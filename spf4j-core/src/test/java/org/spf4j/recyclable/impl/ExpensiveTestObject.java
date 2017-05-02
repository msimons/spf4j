 /*
 * Copyright (c) 2001, Zoltan Farkas All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.spf4j.recyclable.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a pooled object implementation, that behaves like a connection object.
 *
 * @author zoly
 */
@SuppressFBWarnings("PREDICTABLE_RANDOM") //not security related
public final class ExpensiveTestObject implements Closeable {

    private static final AtomicInteger OBJ_COUNT = new AtomicInteger();

    private final long maxIdleMillis;
    private final int nrUsesToFailAfter;
    private final long minOperationMillis;
    private final long maxOperationMillis;
    private long lastTouchedTimeMillis;
    private int nrUses;
    private final String id;

    @SuppressFBWarnings("STT_TOSTRING_STORED_IN_FIELD")
    public ExpensiveTestObject(final long maxIdleMillis, final int nrUsesToFailAfter,
            final long minOperationMillis, final long maxOperationMillis) {
        this.maxIdleMillis = maxIdleMillis;
        this.nrUsesToFailAfter = nrUsesToFailAfter;
        this.minOperationMillis = minOperationMillis;
        this.maxOperationMillis = maxOperationMillis;
        lastTouchedTimeMillis = System.currentTimeMillis();
        nrUses = 0;
        simulateDoStuff(maxOperationMillis - minOperationMillis);
        id = "Test Object " + OBJ_COUNT.getAndIncrement();
    }

    public void doStuff() throws IOException {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTouchedTimeMillis > maxIdleMillis) {
            throw new IOExceptionImpl("Connection closed for " + id);
        }
        if (nrUses > nrUsesToFailAfter) {
            throw new IOExceptionImpl("Simulated random crap " + id);
        }
        simulateDoStuff(maxOperationMillis - minOperationMillis);
        nrUses++;
        lastTouchedTimeMillis = System.currentTimeMillis();
    }

    public void testObject() throws IOException {
        System.out.println("Test oid " + id);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTouchedTimeMillis > maxIdleMillis) {
            throw new IOException("Connection closed " + id);
        }
        if (nrUses > nrUsesToFailAfter) {
            throw new IOExceptionImpl("Simulated random crap " + id);
        }
        simulateDoStuff(0);
        nrUses++;
        lastTouchedTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void close() throws IOException {
        doStuff();
    }

    @SuppressFBWarnings("MDM_THREAD_YIELD")
    private void simulateDoStuff(final long time) {
        long sleepTime = (long) (Math.random() * (time));
        try {
            Thread.sleep(minOperationMillis + sleepTime);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

  @Override
  public String toString() {
    return "ExpensiveTestObject{" + "maxIdleMillis=" + maxIdleMillis + ", nrUsesToFailAfter="
            + nrUsesToFailAfter + ", minOperationMillis=" + minOperationMillis + ", maxOperationMillis="
            + maxOperationMillis + ", lastTouchedTimeMillis=" + lastTouchedTimeMillis + ", nrUses=" + nrUses
            + ", id=" + id + '}';
  }

  private static final class IOExceptionImpl extends IOException {

    public IOExceptionImpl(String message) {
      super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
      return this;
    }
  }



}
