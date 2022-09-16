package seedu.address.model.person;

public class Remark {
    public final String value;

    public Remark(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || other instanceof Remark // instanceof to handle null
                && value.equals((((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
}
