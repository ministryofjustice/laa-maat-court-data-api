package gov.uk.courtdata.converter;

import jakarta.persistence.Converter;

import org.hibernate.type.YesNoConverter;

@Converter(autoApply = true)
public class AutoApplyYesNoConverter extends YesNoConverter {}
