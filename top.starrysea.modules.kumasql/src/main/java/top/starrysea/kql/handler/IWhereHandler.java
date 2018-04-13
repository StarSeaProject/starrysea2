package top.starrysea.kql.handler;

import java.util.List;

import top.starrysea.kql.clause.WhereClause;

@FunctionalInterface
public interface IWhereHandler {

	HandleResult handleWhereBuffer(WhereClause where, List<Object> preParams);
}
