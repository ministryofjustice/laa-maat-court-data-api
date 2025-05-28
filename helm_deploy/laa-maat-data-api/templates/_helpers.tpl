{{/*
Expand the name of the chart.
*/}}
{{- define "laa-maat-data-api.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "laa-maat-data-api.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "laa-maat-data-api.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "laa-maat-data-api.labels" -}}
helm.sh/chart: {{ include "laa-maat-data-api.chart" . }}
{{ include "laa-maat-data-api.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "laa-maat-data-api.selectorLabels" -}}
app.kubernetes.io/name: {{ include "laa-maat-data-api.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "laa-maat-data-api.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "laa-maat-data-api.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create ingress configuration
*/}}
{{- define "laa-maat-data-api.ingress" -}}
{{- $internalAllowlistSourceRange := (lookup "v1" "Secret" .Release.Namespace "maat-api-env-variables").data.INTERNAL_ALLOWLIST_SOURCE_RANGE | b64dec }}
{{- if $internalAllowlistSourceRange }}
  nginx.ingress.kubernetes.io/whitelist-source-range: {{ $internalAllowlistSourceRange }}
  external-dns.alpha.kubernetes.io/set-identifier: {{ include "laa-maat-data-api.fullname" . }}-{{ $.Values.ingress.environmentName}}-green
{{- end }}
{{- end }}

{{/*
Create external ingress configuration
*/}}
{{- define "laa-maat-data-api.externalIngress" -}}
{{- $externalAllowlistSourceRange := (lookup "v1" "Secret" .Release.Namespace "maat-api-env-variables").data.EXTERNAL_ALLOWLIST_SOURCE_RANGE | b64dec }}
{{- if $externalAllowlistSourceRange }}
  nginx.ingress.kubernetes.io/whitelist-source-range: {{ $externalAllowlistSourceRange }}
  external-dns.alpha.kubernetes.io/set-identifier: {{ include "laa-maat-data-api.fullname" . }}-external-{{ $.Values.ingress.environmentName}}-green
{{- end }}
{{- end }}
