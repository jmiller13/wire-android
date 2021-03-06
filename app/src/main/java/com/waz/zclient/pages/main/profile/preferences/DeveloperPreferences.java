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
package com.waz.zclient.pages.main.profile.preferences;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.view.WindowManager;
import android.widget.Toast;
import com.waz.zclient.R;
import com.waz.zclient.core.controllers.tracking.events.Event;
import com.waz.zclient.pages.BasePreferenceFragment;
import com.waz.zclient.utils.DebugUtils;


public class DeveloperPreferences extends BasePreferenceFragment<DeveloperPreferences.Container> {

    public static final String TAG = DeveloperPreferences.class.getName();
    private Preference lastCallSessionIdPreference;

    public static DeveloperPreferences newInstance(String rootKey, Bundle extras) {
        DeveloperPreferences f = new DeveloperPreferences();
        Bundle args = extras == null ? new Bundle() : new Bundle(extras);
        args.putString(ARG_PREFERENCE_ROOT, rootKey);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences2(savedInstanceState, rootKey);
        addPreferencesFromResource(R.xml.preferences_developer);
        lastCallSessionIdPreference = findPreference(getString(R.string.pref_dev_avs_last_call_session_id_key));
        lastCallSessionIdPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                copyLastCallSessionIdToClipboard();
                return true;
            }
        });
        final String lastCallSessionId = preferenceManager.getSharedPreferences()
                                                          .getString(getString(R.string.pref_dev_avs_last_call_session_id_key),
                                                                     getString(R.string.pref_dev_avs_last_call_session_id_not_available));
        lastCallSessionIdPreference.setSummary(lastCallSessionId);

        Preference versionInfoPrefs = findPreference(getString(R.string.pref_dev_version_info_id_key));
        versionInfoPrefs.setSummary(DebugUtils.getVersion(getContext()));
    }

    private void copyLastCallSessionIdToClipboard() {
        final String lastCallSessionIdKey = getString(R.string.pref_dev_avs_last_call_session_id_key);
        String lastCallSessionId = preferenceManager.getSharedPreferences().getString(lastCallSessionIdKey,
                                                                                      getString(R.string.pref_dev_avs_last_call_session_id_not_available));
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.pref_dev_avs_last_call_session_id_title),
                                              lastCallSessionId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(),
                       getString(R.string.pref_dev_avs_last_call_session_id_copied_to_clipboard),
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        lastCallSessionIdPreference.setOnPreferenceClickListener(null);
        lastCallSessionIdPreference = null;
        super.onDestroyView();
    }

    @Override
    public Event handlePreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_dev_status_bar_key))) {
            if (sharedPreferences.getBoolean(key, true)) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        } else if (key.equals(getString(R.string.pref_dev_avs_last_call_session_id_key))) {
            String lastCallSessionId = sharedPreferences.getString(key,
                                                                   getString(R.string.pref_dev_avs_last_call_session_id_not_available));
            lastCallSessionIdPreference.setSummary(lastCallSessionId);
        }
        return null;
    }

    public interface Container {
    }
}
