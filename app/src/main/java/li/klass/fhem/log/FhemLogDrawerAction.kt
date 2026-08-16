/*
 *  AndFHEM - Open Source Android application to control a FHEM home automation
 *  server.
 *
 *  Copyright (c) 2011, Matthias Klass or third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by Red Hat Inc.
 *
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU GENERAL PUBLIC LICENSE, as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU GENERAL PUBLIC LICENSE
 *  for more details.
 *
 *  You should have received a copy of the GNU GENERAL PUBLIC LICENSE
 *  along with this distribution; if not, write to:
 *    Free Software Foundation, Inc.
 *    51 Franklin Street, Fifth Floor
 *    Boston, MA  02110-1301  USA
 */

package li.klass.fhem.log

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import li.klass.fhem.AndFHEMApplication
import li.klass.fhem.R
import li.klass.fhem.activities.drawer.actions.AbstractDrawerAction
import li.klass.fhem.constants.Actions
import li.klass.fhem.util.DialogUtil
import java.io.File
import javax.inject.Inject

class FhemLogDrawerAction @Inject constructor(
        private val fhemLogService: FhemLogService
) : AbstractDrawerAction(R.id.fhem_log) {
    override fun execute(activity: AppCompatActivity) {
        activity.sendBroadcast(Intent(Actions.SHOW_EXECUTING_DIALOG).apply { setPackage(AndFHEMApplication.application?.packageName) })
        GlobalScope.launch(Dispatchers.Main) {
            val temporaryFile = withContext(Dispatchers.IO) {
                fhemLogService.getLogAndWriteToTemporaryFile()
            }
            activity.sendBroadcast(Intent(Actions.DISMISS_EXECUTING_DIALOG).apply { setPackage(AndFHEMApplication.application?.packageName) })
            handle(activity, temporaryFile)
        }
    }

    private fun handle(activity: Activity, file: File?) {
        if (file != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(activity, "${activity.packageName}.AndFHEMFileProvider", file)
            intent.setDataAndType(uri, "text/plain")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(intent)
        } else {
            DialogUtil.showAlertDialog(activity, R.string.fhem_log, R.string.fhem_log_error)
        }
    }
}