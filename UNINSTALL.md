# Uninstalling root

    adb shell

    mount -o remount,rw /vendor
    rm /vendor/bin/check_1860_state.sh
    mount -o remount,ro /vendor

    reboot
