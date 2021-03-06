/*
 * Copyright (c) 2001-2017, Zoltan Farkas All Rights Reserved.
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
 *
 * Additionally licensed with:
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spf4j.log;

import org.spf4j.base.avro.LogLevel;


/**
 * An enum for log levels
 * @author Zoltan Farkas
 */
public enum Level {
  TRACE(java.util.logging.Level.FINEST, LogLevel.TRACE),
  DEBUG(java.util.logging.Level.FINE, LogLevel.DEBUG),
  INFO(java.util.logging.Level.INFO, LogLevel.INFO),
  WARN(java.util.logging.Level.WARNING, LogLevel.WARN),
  ERROR(java.util.logging.Level.SEVERE, LogLevel.ERROR),
  OFF(java.util.logging.Level.OFF, LogLevel.UNKNOWN);

  private final java.util.logging.Level julLevel;

  private final LogLevel avroLevel;

  Level(final java.util.logging.Level julLevel, final LogLevel avroLevel) {
    this.julLevel = julLevel;
    this.avroLevel = avroLevel;
  }

  public int getIntValue() {
    return julLevel.intValue();
  }

  public java.util.logging.Level getJulLevel() {
    return julLevel;
  }

  public LogLevel getAvroLevel() {
    return avroLevel;
  }

  public static Level fromAvroLevel(final LogLevel level) {
    switch (level) {
      case UNKNOWN:
      case DEBUG:
        return Level.DEBUG;
      case ERROR:
        return Level.ERROR;
      case INFO:
        return Level.INFO;
      case TRACE:
        return Level.TRACE;
      case WARN:
        return Level.WARN;
      default:
        throw new IllegalArgumentException("Unsupported LogLevel " + level);
    }
  }


  public static Level fromJulLevel(final int severity) {
     if (severity <= TRACE.getIntValue()) {
          return TRACE;
    } else if (severity <= DEBUG.getIntValue()) {
      return DEBUG;
    } else if (severity <= INFO.getIntValue()) {
      return INFO;
    } else if (severity <= WARN.getIntValue()) {
      return WARN;
    } else if (severity <= ERROR.getIntValue()) {
      return ERROR;
    } else {
      return OFF;
    }
  }

}

