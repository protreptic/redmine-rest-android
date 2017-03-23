package name.peterbukhal.android.redmine.rest.service.request;

/**
 * Created by
 *
 * @author Peter Bukhal petr.bukhal <at> doconcall.ru
 *         on 23.03.2017.
 */
public final class IssueRequest {

    class Builder {

        private int offset;
        private int limit;

        private int id;

        public Builder withId(int id) {
            this.id = id;

            return this;
        }

        public Builder withOffset(int offset) {
            this.offset = offset;

            return this;
        }

        public Builder withLimit(int limit) {
            this.limit = limit;

            return this;
        }


        public void build() {

        }

    }

}
