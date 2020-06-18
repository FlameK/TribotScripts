package scripts.teleTabMaker.data;

public class Vars {
    public String status;

    private Vars() {}
        private static Vars vars = new Vars();
        public static Vars get() {
            return vars;
        }

        private static Vars VARS = new Vars();
        public boolean shouldRun = true;

}

