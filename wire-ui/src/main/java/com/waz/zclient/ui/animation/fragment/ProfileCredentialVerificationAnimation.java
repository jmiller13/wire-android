/**
 * Wire
 * Copyright (C) 2016 Wire Swiss GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.waz.zclient.ui.animation.fragment;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.waz.zclient.ui.animation.interpolators.penner.Expo;

public class ProfileCredentialVerificationAnimation extends Animation {
    private float fromAlpha;
    private float toAlpha;

    public ProfileCredentialVerificationAnimation(int duration, float fromAlpha, float toAlpha) {
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;

        setInterpolator(new Expo.EaseOut());
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.setAlpha(fromAlpha + ((toAlpha - fromAlpha) * interpolatedTime));
        super.applyTransformation(interpolatedTime, t);
    }
}
