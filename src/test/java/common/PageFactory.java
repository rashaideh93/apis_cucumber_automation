package common;


public class PageFactory {

    private static PageFactory _pageFactory;

    private PageFactory() {
        super();
    }

    public static PageFactory instance() {
        if (_pageFactory == null) {
            _pageFactory = new PageFactory();
        }
        return _pageFactory;
    }
}
