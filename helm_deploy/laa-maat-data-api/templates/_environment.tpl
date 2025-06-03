{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for service containers
*/}}
{{- define "laa-maat-data-api.env-vars" }}
env:
  - name: AWS_REGION
    value: {{ .Values.aws_region }}
  - name: SENTRY_DSN
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: SENTRY_DSN
  - name: SENTRY_ENV
    value: {{ .Values.java.host_env }}
  - name: SENTRY_SAMPLE_RATE
    value: {{ .Values.sentry.sampleRate | quote }}
  - name: LOG_LEVEL
    value: {{ .Values.logging.level }}
  - name: CDA_BASE_URL
    value: {{ .Values.cdaApi.baseUrl }}
  - name: CDA_OAUTH_URL
    value: {{ .Values.cdaApi.oauthUrl }}
  - name: JWT_ISSUER_URI
    value: {{ .Values.jwt.issuerUri }}
  - name: SCOPE_MAATAPI
    value: {{ .Values.scope }}
  - name: CLOUD_PLATFORM_QUEUE_REGION
    value: {{ .Values.cloudPlatform.aws.sqs.region }}
  - name: AWS_DEFAULT_REGION
    value: {{ .Values.aws_region }}
  - name: POST_MVP_ENABLED
    value: "{{ .Values.postMvpEnabled }}"
  - name: CDA_OAUTH_CLIENT_ID
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: CDA_API_OAUTH_CLIENT_ID
  - name: CDA_OAUTH_CLIENT_SECRET
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: CDA_API_OAUTH_CLIENT_SECRET
  - name: TOGDATA_DATASOURCE_USERNAME
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: TOGDATA_DATASOURCE_USERNAME
  - name: TOGDATA_DATASOURCE_PASSWORD
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: TOGDATA_DATASOURCE_PASSWORD
  - name: DATASOURCE_URL
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: DATASOURCE_URL
  - name: DATASOURCE_USERNAME
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: DATASOURCE_USERNAME
  - name: DATASOURCE_PASSWORD
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: DATASOURCE_PASSWORD
  - name: CREATE_LINK_QUEUE
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: CREATE_LINK_QUEUE
  - name: UNLINK_QUEUE
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: UNLINK_QUEUE
  - name: HEARING_RESULTED_QUEUE
    valueFrom:
        secretKeyRef:
            name: maat-api-env-variables
            key: HEARING_RESULTED_QUEUE
{{- end -}}
