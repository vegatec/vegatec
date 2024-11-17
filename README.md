# vegatec


issue:
  elastic search was failing to lock node 
resolution:
  moved data and logs to a volume on the host and changed permission to 777

issue:
 mysql docker failed to upgrade
resolution:
 commented mysql.yml config line