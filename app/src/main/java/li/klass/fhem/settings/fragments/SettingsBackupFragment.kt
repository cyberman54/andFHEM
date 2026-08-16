package li.klass.fhem.settings.fragments

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceClickListener
import androidx.preference.PreferenceFragmentCompat
import li.klass.fhem.AndFHEMApplication
import li.klass.fhem.R
import li.klass.fhem.backup.ui.ImportExportUIService
import li.klass.fhem.settings.SettingsKeys
import javax.inject.Inject

class SettingsBackupFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var importExportUIService: ImportExportUIService

    private val importBackup = registerForActivityResult(ActivityResultContracts.OpenDocument()) { file ->
        val activity = activity ?: return@registerForActivityResult
        file?.let { importExportUIService.onImportFileSelected(it, activity) }
    }

    private val exportBackup = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { file ->
        val activity = activity ?: return@registerForActivityResult
        file?.let { importExportUIService.onExportFileSelected(it, activity) }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_backup, rootKey)
        AndFHEMApplication.application?.daggerComponent?.inject(this)

        findPreference<Preference>(SettingsKeys.EXPORT_SETTINGS)?.apply {
            onPreferenceClickListener = OnPreferenceClickListener {
                exportBackup.launch(importExportUIService.backupFileName)
                true
            }
        }
        findPreference<Preference>(SettingsKeys.IMPORT_SETTINGS)?.apply {
            onPreferenceClickListener = OnPreferenceClickListener {
                importBackup.launch(arrayOf("application/octet-stream", "application/x-zip"))
                true
            }
        }
    }

}