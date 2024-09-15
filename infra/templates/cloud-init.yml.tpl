#cloud-config
package_update: true
package_upgrade: true

packages:
  - openjdk-21-jdk

write_files:
  - content: |
      ${run_script}
    encoding: b64
    owner: root:root
    path: /opt/run.sh
    permissions: '0750'

runcmd:

  - |
    curl -o openjdk.tar.gz https://download.java.net/openjdk/jdk21/ri/openjdk-21+35_linux-x64_bin.tar.gz
    tar -xzvf openjdk.tar.gz
    mv jdk-21 /opt/jdk
    /opt/run.sh > /var/log/api.log 2>&1
    EOF


