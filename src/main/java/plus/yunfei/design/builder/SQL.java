package plus.yunfei.design.builder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SQL {
    private SQL() {
    }

    public static SelectBuilder select(String... column) {
        return new SelectBuilder(column);
    }

    public static TableStage update() {
        return new UpdateBuilder();
    }

    private static class SelectBuilder {
        private String[] columns;
        private String table;
        private String where;

        public SelectBuilder(String[] columns) {
            this.columns = columns;
        }

        public SelectBuilder from(String table) {
            this.table = table;
            return this;
        }

        public SelectBuilder where(String where) {
            this.where = where;
            return this;
        }

        public String buildSql() {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ").append(String.join(",", columns)).append(" FROM ").append(table);
            if (where != null) {
                sql.append(" WHERE ").append(where);
            }
            return sql.toString();
        }
    }


    interface TableStage {
        WhereStage table(String table);
    }

    interface WhereStage {
        SetStage where(String where);
    }

    interface SetStage {
        SetStage set(String key, String value);

        String buildSql();
    }

    private static class UpdateBuilder implements TableStage, WhereStage, SetStage {
        private String table;
        private String where;
        private Map<String, String> setMap = new LinkedHashMap<>();

        public WhereStage table(String table) {
            this.table = table;
            return this;
        }

        public SetStage where(String where) {
            this.where = where;
            return this;
        }

        public SetStage set(String column, String value) {
            setMap.put(column, value);
            return this;
        }

        public String buildSql() {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ").append(table).append(" SET ");
            String setString = setMap.entrySet().stream().map(entry -> {
                return entry.getKey() + "=" + entry.getValue();
            }).collect(Collectors.joining(","));
            sql.append(setString);
            if (where != null) {
                sql.append(" WHERE ").append(where);
            }
            return sql.toString();
        }
    }

    public static void main(String[] args) {
        String selectSql = SQL.select("id", "name")
                .from("user")
                .where("id=1")
                .buildSql();
        System.out.println(selectSql);

        String updateSql = SQL.update()
                .table("user")
                .where("id=1")
                .set("name", "张三")
                .set("age", "18")
                .buildSql();
        System.out.println(updateSql);
    }

}
