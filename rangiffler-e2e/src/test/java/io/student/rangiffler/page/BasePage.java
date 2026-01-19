package io.student.rangiffler.page;

public abstract class BasePage<T extends BasePage<T>> {

    protected abstract void verifyPageOpened();

    public final T shouldBeOpened() {
        verifyPageOpened();
        return self();
    }

    @SuppressWarnings("unchecked")
    protected final T self() {
        return (T) this;
    }
}
