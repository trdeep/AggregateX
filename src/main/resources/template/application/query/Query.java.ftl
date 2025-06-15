package ${packageName}.${moduleNameLower}.application.query;

import ${packageName}.${moduleNameLower}.application.query.result.ListQueryResult;
import cn.treedeep.king.core.application.cqrs.query.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SayHelloQuery extends Query<ListQueryResult> {

    @Getter
    private String name;

    @Override
    public String getQueryName() {
        return "SayHelloQuery";
    }
}
