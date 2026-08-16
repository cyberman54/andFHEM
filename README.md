andFHEM
=======

AndFHEM is an Android frontend to control devices using an FHEM home automation server. For details see http://andFHEM.klass.li.

andFHEM was declared deprecated by its original maintainer; see the respective
[post in the FHEM forum](https://forum.fhem.de/index.php/topic,127410.0.html).
Compatibility and security maintenance does not imply renewed feature development.

The app currently targets Android 16 (API 36) while retaining Android 6.0
(API 23) as its minimum supported version.

## Connection security

FHEM installations commonly run on local networks with HTTP or private
certificate authorities. The app therefore deliberately permits cleartext
connections and user-installed CAs. Prefer HTTPS and review certificates
carefully whenever the server supports it. Android cloud backup is disabled;
settings can be transferred explicitly through the encrypted backup
import/export flow.

## Contributing

![](https://github.com/klassm/andFHEM/workflows/Android%20CI/badge.svg)

See [Contributing](CONTRIBUTING.md) for details.
