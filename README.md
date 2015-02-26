# Vivantech's Kuali Coeus Fixes Project

## Overview
This project contains [Vivantech](www.vivantech.com)'s publicly available fixes to the [Kuali Foundation](www.kuali.org)'s [Kuali Coeus](www.kuali.org/kc) application.  It is designed as a maven war overlay to the Foundation's code.  The base war can be found in Vivantech's [public maven repository](https://jenkins.dev.ekualiti.com/nexus/content/repositories/public/).  The current base war is KC version 5.2.1 and is built from code which can be found at https://github.com/kuali/kc/tree/coeus-5.2.1.

To update the databases, run the liquibase installation by running (from the kc_fixes directory):
```bash
mvn install \
  -Pupdate-kc,update-rice \
  -Dkc.liquibase.username=REPLACE \
  -Dkc.liquibase.password=REPLACE \
  -Drice.liquibase.username=REPLACE \
  -Drice.liquibase.password=REPLACE
```
