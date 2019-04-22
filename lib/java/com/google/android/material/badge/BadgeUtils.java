/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.google.android.material.badge;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Utility class for {@link BadgeDrawable}.
 *
 * @hide
 */
@RestrictTo(Scope.LIBRARY)
public class BadgeUtils {

  private BadgeUtils() {
    // Private constructor to prevent unwanted construction.
  }

  /**
   * Updates a badge's bounds using its center coordinate, {@code halfWidth} and {@code halfHeight}.
   *
   * @param rect Holds rectangular coordinates of the badge's bounds.
   * @param centerX A badge's center x coordinate.
   * @param centerY A badge's center y coordinate.
   * @param halfWidth Half of a badge's width.
   * @param halfHeight Half of a badge's height.
   */
  public static void updateBadgeBounds(
      Rect rect, float centerX, float centerY, float halfWidth, float halfHeight) {
    rect.set(
        (int) (centerX - halfWidth),
        (int) (centerY - halfHeight),
        (int) (centerX + halfWidth),
        (int) (centerY + halfHeight));
  }

  /*
   * Attaches a BadgeDrawable to its associated anchor and update the BadgeDrawable's coordinates
   * based on the anchor.
   * For API 18+, the BadgeDrawable will be added as a view overlay.
   * For pre-API 18, the BadgeDrawable will be set as the foreground of a FrameLayout that is an
   * ancestor of the anchor.
   */
  public static void attachBadgeDrawable(
      BadgeDrawable badgeDrawable, View anchor, FrameLayout preApi18BadgeParent) {
    setBadgeDrawableBounds(badgeDrawable, anchor, preApi18BadgeParent);
    if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR2) {
      preApi18BadgeParent.setForeground(badgeDrawable);
    } else {
      anchor.getOverlay().add(badgeDrawable);
    }
  }

  /*
   * Detaches a BadgeDrawable to its associated anchor.
   * For API 18+, the BadgeDrawable will be removed from its anchor's ViewOverlay.
   * For pre-API 18, the BadgeDrawable will be removed from the foreground of a FrameLayout that is
   * an ancestor of the anchor.
   */
  public static void detachBadgeDrawable(
      @Nullable BadgeDrawable badgeDrawable, View anchor, FrameLayout preApi18BadgeParent) {
    if (badgeDrawable == null) {
      return;
    }
    if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR2) {
      preApi18BadgeParent.setForeground(null);
    } else {
      anchor.getOverlay().remove(badgeDrawable);
    }
  }

  /**
   * Sets the bounds of a BadgeDrawable to match the bounds of its anchor (for API 18+) or its
   * anchor's FrameLayout ancestor (pre-API 18).
   */
  public static void setBadgeDrawableBounds(
      BadgeDrawable badgeDrawable, View anchor, FrameLayout preApi18BadgeParent) {
    Rect badgeBounds = new Rect();
    if (VERSION.SDK_INT < VERSION_CODES.JELLY_BEAN_MR2) {
      preApi18BadgeParent.getDrawingRect(badgeBounds);
    } else {
      anchor.getDrawingRect(badgeBounds);
    }
    badgeDrawable.setBounds(badgeBounds);
    badgeDrawable.updateBadgeCoordinates(anchor, preApi18BadgeParent);
  }
}
