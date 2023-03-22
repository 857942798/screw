/**
 * 我们一般通过会话上下文来获取会话,而不同的web框架的会话对象可能并不相同（可能为非Servlet模型）,为了让会话管理模块能够适用于不同的web框架,特封装此包下的包装类进行对接
 */
package com.ds.screw.auth.context.domain;