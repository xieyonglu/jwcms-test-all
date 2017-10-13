package jwcms.test.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneConstraint implements ConstraintValidator<Phone, String> {

	private String regexp;

	@Override
	public void initialize(Phone phoneAnnotation) {
		this.regexp = phoneAnnotation.regexp();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // HV000028: Unexpected exception during isValid call
		}

		if (value.matches(regexp)) {
			return true;
		}
		return false;
	}

}
