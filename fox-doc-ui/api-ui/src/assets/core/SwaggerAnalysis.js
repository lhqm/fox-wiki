import logUtil from '../utils/logUtil.js';
// 无需特殊处理的参数类型
let notNeedHandleTypeArr = ['file', 'string', 'integer', 'long', 'double', 'object', 'number', 'boolean', 'ref'];

/**
 * swagger格式的参数解析参数解析
 * @author 离狐千慕
 * @since 2017年5月7日
 */
export default {
    /**
     * 解析请求的参数
     * @param parameters swagger.parameters
     * @param definitionsDataMap 解析的path里对应的数据map，{url + "." + method: swagger.paths.post}
     * @returns [] 参数列表：[{
     *     type: '',
     *     key: '',
     *     in: '',
     *     name: '',
     *     subType: '',
     *     required: '否',
     *     format: '',
     *     enum: '',
     *     example: '',
     *     collectionFormat: 'multi',
     *     description: '',
     *     additional: '',
     *     children: [],
     * }]
     */
    getRequestParamList(parameters, definitionsDataMap) {
        if (!parameters) {
            return [];
        }
        let indexKey = 1;
        let requestParamList = [];
        for (let i = 0; i < parameters.length; i++) {
            let parameter = parameters[i];
            let description = parameter.description || '';
            let type = parameter.type;
            let format = parameter.format;
            let example = parameter['x-example'];
            let subType = undefined;
            let children = undefined;
            let additional = undefined;
            if (type === 'array') {
                // 解析parameter.items.$ref 或 parameter.items.$ref {$ref: "#/definitions/Model"}
                // 解析parameter.items.type {type: 'file'}
                if (this.isSchemaRef(parameter.items)) {
                    subType = this.getSchemaRef(parameter.items);
                    children = this.getParamDefinitions(subType, definitionsDataMap, indexKey, {}, 0);
                } else if (parameter.schema) {
                    if (this.isSchemaRef(parameter.schema.items)) {
                        subType = this.getSchemaRef(parameter.schema.items);
                        children = this.getParamDefinitions(subType, definitionsDataMap, indexKey, {}, 0);
                    } else if (parameter.schema.type) {
                        subType = parameter.schema.type;
                    }
                } else if (parameter.items && parameter.items.type) {
                    subType = parameter.items.type;
                } else {
                    logUtil.logMessage('001', type, parameter);
                }
            } else if (!type) {
                if (parameter.schema) {
                    if (this.isSchemaRef(parameter.schema)) {
                        // 解析parameter.schema {$ref: "#/definitions/Model"}
                        type = this.getSchemaRef(parameter.schema);
                        children = this.getParamDefinitions(type, definitionsDataMap, indexKey, {}, 0);
                    } else if (parameter.schema.type) {
                        type = parameter.schema.type;
                        if (parameter.schema.additionalProperties) {
                            additional = {};
                            children = this.getAdditionalProperties(parameter.schema.additionalProperties, additional, definitionsDataMap, indexKey, {}, 0);
                            format = additional.type;
                        } else if (parameter.schema.items) {
                            if (this.isSchemaRef(parameter.schema.items)) {
                                subType = this.getSchemaRef(parameter.schema.items);
                                children = this.getParamDefinitions(subType, definitionsDataMap, indexKey, {}, 0);
                            } else if (parameter.schema.items.type) {
                                subType = parameter.schema.items.type;
                            } else {
                                logUtil.log('0014', type, parameter);
                            }
                        } else {
                            logUtil.log('0011', type, parameter);
                        }
                    } else {
                        logUtil.logMessage('0013', type, parameter);
                    }
                } else if (parameter.items && parameter.items.type) {
                    // 解析parameter.items {type: "object", $ref: "#/definitions/Model"}
                    type = parameter.items.type;
                    if (parameter.items.additionalProperties) {
                        additional = {};
                        children = this.getAdditionalProperties(parameter.items.additionalProperties, additional, definitionsDataMap, indexKey, {}, 0);
                        format = additional.type;
                    } else {
                        logUtil.logMessage('0012', type, parameter);
                    }
                } else {
                    logUtil.logMessage('002', type, parameter);
                }
            } else {
                if (notNeedHandleTypeArr.indexOf(type) >= 0) {
                    // 无需特殊处理的类型
                } else {
                    logUtil.logMessage('003', type, parameter);
                }
            }
            if (example) {
                description = description ? (description + '，') : '';
                description += '例：' + example;
            }
            if (parameter.enum && parameter.enum.length > 0) {
                description = description || '枚举类型';
                description += '，可选值：' + parameter.enum.join('、');
            }
            requestParamList.push({
                type: type,
                key: indexKey,
                in: parameter.in,
                name: parameter.name,
                subType: subType,
                required: parameter.required ? '是' : '否',
                format: format,
                enum: parameter.enum,
                example: example,
                collectionFormat: parameter.collectionFormat,// 枚举多选时=multi
                description: description,
                additional: additional,
                children: children,
            });
            indexKey++;
        }
        return requestParamList;
    },
    /**
     * 解析请求返回的结果集
     * @param responses swagger.parameters
     * @param definitionsDataMap 解析的path里对应的数据map，{url + "." + method: swagger.paths.post}
     * @returns [] 参数列表：[{
     *     code: '',
     *     type: '',
     *     key: '',
     *     desc: '',
     *     schemas: [],
     * }]
     */
    getResponseParamList(responses, definitionsDataMap) {
        let responsesList = [];
        let indexKey = 1;
        Object.keys(responses).forEach(code => {
            let codeResponses = responses[code];
            let type = undefined;
            let children = undefined;
            if (this.isSchemaRef(codeResponses.schema)) {
                type = this.getSchemaRef(codeResponses.schema);
                children = this.getParamDefinitions(type, definitionsDataMap, indexKey, {}, 0);
            }
            responsesList.push({
                code: code,
                type: type,
                key: indexKey,
                desc: codeResponses.description,
                schemas: children,
            });
            indexKey++;
        });
        return responsesList;
    },
    /**
     * 判断是否包含$ref属性
     * @param schema
     * @returns {boolean}
     */
    isSchemaRef(schema) {
        return !!(schema && schema['$ref']);
    },
    /**
     * 获取对象的$ref属性
     * @param schema
     * @returns {string}
     */
    getSchemaRef(schema) {
        if (schema['$ref'] && schema['$ref'].indexOf('#/definitions/') === 0) return schema['$ref'].replace('#/definitions/', '');
        logUtil.logMessage('9467', '', schema);
        return '';
    },
    /**
     * 获取swagger.definitions里的对象信息
     * @param ref 对象名
     * @param definitionsDataMap 解析的path里对应的数据map，{url + "." + method: swagger.paths.post}
     * @param indexKey 层级key
     * @param parentRef 父级已用过的ref，防止无限递归
     * @param deep 层级深度，大于10层则不再解析，防止层级太深或无线递归
     * @returns {undefined|
     * {
     *      type: '',
     *      name: '',
     *      key: '',
     *      subType: '',
     *      format: '',
     *      description: '',
     *      enum: '',
     *      additional: '',
     *      example: '',
     *      children: [],
     * }
     * }
     */
    getParamDefinitions(ref, definitionsDataMap, indexKey, parentRef, deep) {
        let definition = definitionsDataMap[ref];
        // 层级大于5层 或者 没有类型定义
        if (deep >= 10 || !definition) {
            return undefined;
        }
        // 允许重复递归一次
        parentRef[ref] = (parentRef[ref] || 0) + 1;
        if (parentRef[ref] > 2) {
            return undefined;
        }
        let paramList = [];
        let type = definition.type;
        let properties = definition.properties;
        let indexSub = 1;
        if (type === 'object' && properties) {
            let currentLevelTypes = {};
            Object.keys(properties).forEach(key => {
                let parameter = properties[key];
                let type = parameter.type;
                let format = parameter.format;
                let description = parameter.description || '';
                let example = parameter['example'] || parameter['x-example'];
                let subType = undefined;
                let additional = undefined;
                let enums = undefined;
                let keySub = indexKey + '_' + indexSub;
                let children = undefined;
                // 把当前层级用过的类型清除，防止多个同类型在一层，后面的不能解析
                Object.keys(currentLevelTypes).forEach(currentLevelType => {
                    parentRef[currentLevelType] = undefined;
                });
                if (type === 'array') {
                    // 解析parameter.items {$ref: "#/definitions/Model"}
                    if (this.isSchemaRef(parameter.items)) {
                        subType = this.getSchemaRef(parameter.items);
                        children = this.getParamDefinitions(subType, definitionsDataMap, keySub, parentRef, deep + 1);
                    } else if (parameter.items && parameter.items.type) {
                        subType = parameter.items.type;
                    } else {
                        logUtil.logMessage('004', type, parameter);
                    }
                } else if (type === 'object') {
                    if (parameter.additionalProperties) {
                        additional = {};
                        children = this.getAdditionalProperties(parameter.additionalProperties, additional, definitionsDataMap, keySub, parentRef, deep + 1);
                        format = additional.type;
                    } else {
                        logUtil.log('0041', type, parameter);
                    }
                } else if (!type) {
                    if (this.isSchemaRef(parameter)) {
                        type = this.getSchemaRef(parameter);
                        children = this.getParamDefinitions(type, definitionsDataMap, keySub, parentRef, deep + 1);
                    } else {
                        logUtil.logMessage('005', type, parameter);
                    }
                } else {
                    if (notNeedHandleTypeArr.indexOf(type) >= 0) {
                        // 无需特殊处理的类型
                    } else {
                        logUtil.logMessage('006', type, parameter);
                    }
                }
                if (example) {
                    description = description ? (description + '，') : '';
                    description += '例：' + example;
                }
                if (parameter.items && parameter.items.enum && parameter.items.enum.length > 0) {
                    enums = parameter.items.enum;
                    description = description || '枚举类型';
                    description += '，可选值：' + parameter.items.enum.join('、');
                }
                paramList.push({
                    type: type,
                    name: key,
                    key: keySub,
                    subType: subType,
                    format: format,
                    description: description,
                    enum: enums,
                    additional: additional,
                    example: example,
                    children: children,
                });
                indexSub++;
                currentLevelTypes[type] = 1;
            });
        }
        return paramList.length > 0 ? paramList : undefined;
    },
    /**
     * parameter.schema.additionalProperties 类型的参数值处理
     * @param additionalProperties
     * @param additional
     * @param definitionsDataMap
     * @param keySub
     * @param parentRef
     * @param deep
     * @returns {
     *     undefined
     *     |{type: "", name: "", key: "", subType: "", format: "", description: "", enum: "", additional: "", example: "", children: *[]}
     *     |{}
     *     |{children: (undefined|{type: "", name: "", key: "", subType: "", format: "", description: "", enum: "", additional: "", example: "", children: *[]}), type: string}
     * }
     */
    getAdditionalProperties(additionalProperties, additional, definitionsDataMap, keySub, parentRef, deep) {
        if (this.isSchemaRef(additionalProperties)) {
            additional.type = this.getSchemaRef(additionalProperties);
            additional.children = this.getParamDefinitions(additional.type, definitionsDataMap, keySub, parentRef, deep + 1);
            return additional.additional;
        } else if (additionalProperties.additionalProperties) {
            additional.type = additionalProperties.type;
            additional.additional = {};
            return this.getAdditionalProperties(additionalProperties.additionalProperties, additional.additional, definitionsDataMap, keySub, parentRef, deep + 1);
        } else if (additionalProperties.type === 'array') {
            additional.type = additionalProperties.type;
            if (this.isSchemaRef(additionalProperties.items)) {
                let subType = this.getSchemaRef(additionalProperties.items);
                let children = this.getParamDefinitions(subType, definitionsDataMap, keySub, parentRef, deep + 1);
                additional.additional = {
                    type: subType,
                    children: children
                };
                return children;
            } else {
                logUtil.logMessage('007', '', additionalProperties);
            }
        } else {
            additional.type = additionalProperties.type;
            if (notNeedHandleTypeArr.indexOf(additional.type) >= 0) {
                // 无需特殊处理的类型
            } else {
                logUtil.logMessage('008', '', additionalProperties);
            }
        }
        return undefined;
    },
}

