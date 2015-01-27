-- liquibase formatted sql

-- changeset vivantech:minusMIT

update proto_corresp_templ
  set CORRESPONDENCE_TEMPLATE=REPLACE(CORRESPONDENCE_TEMPLATE,'MIT', 'local')
  where PROTO_CORRESP_TEMPL_ID in (7,17);

update proto_corresp_templ
  set CORRESPONDENCE_TEMPLATE=REPLACE(CORRESPONDENCE_TEMPLATE,'COUHES requires that MIT consent forms follow the template on the web site.&#160;', '')
  where PROTO_CORRESP_TEMPL_ID in (13,14);

update proto_corresp_templ
  set CORRESPONDENCE_TEMPLATE=REPLACE(CORRESPONDENCE_TEMPLATE,'Massachusetts Institute of Technology', 'local')
  where PROTO_CORRESP_TEMPL_ID in (13,14);

update proto_corresp_templ
  set CORRESPONDENCE_TEMPLATE=REPLACE(CORRESPONDENCE_TEMPLATE,'Massachusetts Institute of Technology', 'Institutional Review Board')
  where PROTO_CORRESP_TEMPL_ID in (29);
