package li.klass.fhem.infrastructure

import android.Manifest
import android.content.pm.ApplicationInfo
import androidx.test.platform.app.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AndroidMigrationTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val packageInfo = context.packageManager.getPackageInfo(
        context.packageName,
        android.content.pm.PackageManager.GET_PERMISSIONS
    )

    @Test
    fun targetsAndroid16() {
        assertThat(context.applicationInfo.targetSdkVersion).isEqualTo(36)
    }

    @Test
    fun usesScopedStorageAndDeclaresNotificationPermission() {
        assertThat(packageInfo.requestedPermissions).contains(Manifest.permission.POST_NOTIFICATIONS)
        assertThat(packageInfo.requestedPermissions).doesNotContain(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.GET_ACCOUNTS
        )
    }

    @Test
    fun disablesAutomaticBackupOfServerCredentials() {
        assertThat(context.applicationInfo.flags and ApplicationInfo.FLAG_ALLOW_BACKUP).isZero()
    }
}
