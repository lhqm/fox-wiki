import{aa as ee,ab as me,y as n,N as ce,r as i,o as f,b as T,a,w as t,e as y,t as E,c as q,s as J,F as Q,k as r,ac as ve,ad as fe,a6 as _e,v as ye,u as pe,W as he,B as ae,d as Pe,Q as U,X as j}from"./vendor.6399378c.js";import"./index.086d0536.js";import{P as te,a as oe,u as ge,x as be}from"./unitConvert.f432b2c8.js";import{z as X}from"./custom.471d0de0.js";import{a as ke}from"./index.ff4a24c0.js";import{_ as le}from"./main.5fd30069.js";const xe={props:{result:{type:Object,required:!0},loading:{type:Boolean,required:!0}},components:{CloseOutlined:ee,ParamTable:te,ParamBody:oe,aceEditor:ke},setup(p){const{result:o}=me(p);let g=n("body"),e=n("format"),h=n("json"),O=n(""),P=n([]),L=n([]),u=n(""),c=n(""),C=n({}),v=n();const w=()=>{e.value==="preview"&&setTimeout(()=>{v.value&&v.value.contentDocument.write(u.value)},0)},R=()=>{if(u.value="",c.value="",p.result.data){if(C.value=p.result.data,p.result.data.headers){P.value=p.result.data.headers;let d=P.value.find(S=>S.name==="Content-Type");d&&d.value&&(d.value.indexOf("text/html")>=0?h.value="html":d.value.indexOf("text/plain")>=0?h.value="text":d.value.indexOf("application/json")>=0?h.value="json":d.value.indexOf("application/xml")>=0||d.value.indexOf("text/xml")>=0?h.value="xml":d.value.indexOf("application/javascript")>=0&&(h.value="javascript"),O.value=h.value)}if(p.result.data.cookies&&(L.value=p.result.data.cookies),p.result.data.data){u.value=p.result.data.data,c.value=p.result.data.data;try{h.value==="xml"?c.value=be(u.value):h.value==="json"?c.value=JSON.stringify(JSON.parse(u.value),null,4):h.value==="javascript"&&(c.value=JSON.stringify(u.value,null,4))}catch{c.value=p.result.data.data}}else{let d=`
// \u8BF7\u6C42\u5931\u8D25\uFF0C\u4EE5\u4E0B\u4E3A\u5C01\u88C5\u7684\u8FD4\u56DE\u503C\u5BF9\u8C61\uFF0C\u4EC5\u4F9B\u53C2\u8003

`;u.value=d+JSON.stringify(p.result.data),c.value=d+JSON.stringify(p.result.data,null,4)}w()}};return R(),ce(o,()=>R()),{activePage:g,bodyShowType:e,bodyShowTypeChange:w,unitConvert:ge,bodyShowFormatType:h,bodyShowFormatPreview:O,previewHtmlRef:v,resultData:C,resultHeaders:P,resultCookies:L,resultHeadersColumns:[{title:"KEY",dataIndex:"name"},{title:"VALUE",dataIndex:"value"}],resultCookiesColumns:[{title:"KEY",dataIndex:"name"},{title:"VALUE",dataIndex:"value"}],resultDataInit:d=>{d.setFontSize(16)},resultDataContentOrigin:u,resultDataContentFormat:c,resultDataConfig:{wrap:!0,readOnly:!0,autoScrollEditorIntoView:!0,enableBasicAutocompletion:!0,enableSnippets:!0,enableLiveAutocompletion:!0,minLines:30,maxLines:30}}}},we={key:0,style:{"margin-bottom":"30px"}},Se={key:0,style:{"margin-bottom":"30px"}},Ce={style:{"margin-bottom":"10px"}},Re=r("\u683C\u5F0F\u5316"),Le=r("\u539F\u59CB\u503C"),Te=r("\u9884\u89C8"),De=r("JSON"),Oe=r("HTML"),Ie=r("XML"),qe=r("JavaScript"),Ne=r("TEXT"),Ee={key:2},Ue={key:0,ref:"previewHtmlRef",width:"100%",height:"570px",style:{border:"0"}},je={class:"status-info-box"},Ve=r(" \u72B6\u6001\u7801\uFF1A"),Ae=r(" \u8017\u65F6\uFF1A"),Ke=r(" \u5927\u5C0F\uFF1A"),Be={key:1},Fe={style:{color:"#f00"}},Je={class:"status-info-box"},He=r(" \u8017\u65F6\uFF1A"),ze={key:1,style:{"margin-top":"20px"}},Ge={key:2,style:{"margin-top":"20px",color:"#aaa"}};function Me(p,o,g,e,h,O){const P=i("a-radio-button"),L=i("a-radio-group"),u=i("a-select-option"),c=i("a-select"),C=i("ace-editor"),v=i("a-tab-pane"),w=i("a-table"),R=i("a-divider"),b=i("a-tabs"),d=i("a-skeleton"),S=i("a-spin"),N=i("a-empty");return g.result.data?(f(),T("div",we,[g.result.data.data?(f(),T("div",Se,[a(b,{activeKey:e.activePage,"onUpdate:activeKey":o[4]||(o[4]=k=>e.activePage=k),onTabClick:o[5]||(o[5]=()=>{}),style:{padding:"5px 10px 0"}},{rightExtra:t(()=>[y("span",je,[Ve,y("span",null,E(e.resultData.status||"200"),1),a(R,{type:"vertical"}),Ae,y("span",null,E(e.unitConvert.formatSeconds(e.resultData.useTime||0)),1),a(R,{type:"vertical"}),Ke,y("span",null,E(e.unitConvert.formatFileSize(e.resultData.contentLength||0)),1)])]),default:t(()=>[a(v,{tab:"Body",key:"body",forceRender:""},{default:t(()=>[y("div",Ce,[a(L,{value:e.bodyShowType,"onUpdate:value":o[0]||(o[0]=k=>e.bodyShowType=k),onChange:e.bodyShowTypeChange,size:"small"},{default:t(()=>[a(P,{value:"format"},{default:t(()=>[Re]),_:1}),a(P,{value:"row"},{default:t(()=>[Le]),_:1}),a(P,{value:"preview"},{default:t(()=>[Te]),_:1})]),_:1},8,["value","onChange"]),e.bodyShowType==="format"?(f(),q(c,{key:0,value:e.bodyShowFormatType,"onUpdate:value":o[1]||(o[1]=k=>e.bodyShowFormatType=k),size:"small",style:{"margin-left":"10px",width:"100px"}},{default:t(()=>[a(u,{value:"json"},{default:t(()=>[De]),_:1}),a(u,{value:"html"},{default:t(()=>[Oe]),_:1}),a(u,{value:"xml"},{default:t(()=>[Ie]),_:1}),a(u,{value:"javascript"},{default:t(()=>[qe]),_:1}),a(u,{value:"text"},{default:t(()=>[Ne]),_:1})]),_:1},8,["value"])):J("",!0)]),e.bodyShowType==="format"?(f(),q(C,{key:0,value:e.resultDataContentFormat,"onUpdate:value":o[2]||(o[2]=k=>e.resultDataContentFormat=k),onInit:e.resultDataInit,lang:e.bodyShowFormatType,theme:"monokai",width:"100%",height:"100",options:e.resultDataConfig},null,8,["value","onInit","lang","options"])):e.bodyShowType==="row"?(f(),q(C,{key:1,value:e.resultDataContentOrigin,"onUpdate:value":o[3]||(o[3]=k=>e.resultDataContentOrigin=k),onInit:e.resultDataInit,lang:"text",theme:"chrome",width:"100%",height:"100",options:e.resultDataConfig},null,8,["value","onInit","options"])):e.bodyShowType==="preview"?(f(),T("div",Ee,[e.bodyShowFormatPreview==="html"?(f(),T("iframe",Ue,null,512)):(f(),T(Q,{key:1},[r(E(e.resultDataContentOrigin),1)],64))])):J("",!0)]),_:1}),a(v,{tab:"Headers",key:"headers",forceRender:""},{default:t(()=>[a(w,{dataSource:e.resultHeaders,columns:e.resultHeadersColumns,size:"small",pagination:!1,scroll:{y:"300px"}},null,8,["dataSource","columns"])]),_:1}),a(v,{tab:"Cookies",key:"cookies",forceRender:""},{default:t(()=>[a(w,{dataSource:e.resultCookies,columns:e.resultCookiesColumns,size:"small",pagination:!1,scroll:{y:"300px"}},null,8,["dataSource","columns"])]),_:1})]),_:1},8,["activeKey"])])):(f(),T("div",Be,[a(b,{style:{padding:"5px 10px 0"}},{rightExtra:t(()=>[y("span",Je,[He,y("span",null,E(e.unitConvert.formatSeconds(e.resultData.useTime||0)),1)])]),default:t(()=>[a(v,{tab:"\u8BF7\u6C42\u5931\u8D25",key:"body",forceRender:""},{default:t(()=>[y("div",Fe,E(g.result.data.errorMsg),1)]),_:1})]),_:1})]))])):g.loading?(f(),T("div",ze,[a(S,{tip:"\u8BF7\u6C42\u6267\u884C\u4E2D..."},{default:t(()=>[a(d)]),_:1})])):(f(),T("div",Ge,[a(N,{description:"\u70B9\u51FB \u2023\u53D1\u9001\u8BF7\u6C42\u2019 \u83B7\u53D6\u8BF7\u6C42\u7ED3\u679C"})]))}var Qe=le(xe,[["render",Me]]);const Xe={components:{VerticalAlignTopOutlined:ve,VerticalAlignBottomOutlined:fe,CloseOutlined:ee,SaveOutlined:_e,ParamTable:te,ParamBody:oe,ApiRequestResult:Qe},setup(p){let o=n("urlParam");const g=ye(),e=pe(),h={docId:g.query.id,nodeId:g.query.nodeId};let O=e.state.globalParam||[],P=1;const L=n();let u=n([]);const c=n();let C=n([]);const v=n();let w=n([]);const R=n();let b=n([]);const d=n();let S=n([]),N=n(),k=n("form"),H=n("json"),z=n({}),l=n({method:"get"}),G=n({}),K=n(!1);const ne=()=>{if(!l.value.apiUrl){ae.error("\u8BF7\u8F93\u5165\u8BF7\u6C42\u7684\u76EE\u6807URL\u5730\u5740");return}W();const _=new FormData;let D=L.value.getSelectedRowKeys(),V=u.value.filter(s=>D.indexOf(s.key)>=0&&s.name&&s.value).map(s=>s.name+"="+encodeURIComponent(s.value)).join("&"),Z=c.value.getSelectedRowKeys(),A=C.value.filter(s=>Z.indexOf(s.key)>=0&&s.name&&s.value).map(s=>({code:s.name,value:s.value})),$=v.value.getSelectedRowKeys(),F=w.value.filter(s=>$.indexOf(s.key)>=0&&s.name&&s.value).map(s=>({code:s.name,value:s.value})),M=[];if(R.value){let s=R.value.getSelectedRowKeys();M=b.value.filter(x=>s.indexOf(x.key)>=0&&x.name&&x.value).map(x=>({code:x.name,value:x.value}))}let m=[];if(d.value){let s=d.value.getSelectedRowKeys();m=S.value.filter(x=>s.indexOf(x.key)>=0&&x.name&&x.value).map(x=>({code:x.name,value:x.value}))}let I="";N.value&&(I=N.value.getParam());let ue=V?l.value.apiUrl+"?"+V:l.value.apiUrl;_.append("url",ue),_.append("host",""),_.append("method",l.value.method),_.append("contentType",""),_.append("docId",h.docId),_.append("nodeName",l.value.nodeName),_.append("nodeId",h.nodeId),_.append("headerParam",JSON.stringify(A)),_.append("cookieParam",JSON.stringify(F)),_.append("formParam",JSON.stringify(M)),_.append("formEncodeParam",JSON.stringify(m)),_.append("bodyParam",I),K.value=!0,G.value={},X.requestUrl(_).then(s=>{G.value=s,K.value=!1,Y()}).catch(s=>{K.value=!1})};let B=n(!0);const re=()=>{B.value=!1},se=()=>{B.value=!0},ie=()=>{B.value=!0},Y=()=>{e.commit("setCustomRequestChange",{method:l.value.method,nodeId:l.value.nodeId,nodeName:l.value.nodeName}),e.commit("addTableName",{key:g.fullPath,val:l.value.nodeName})};he(async()=>{let _=await X.apiCustomNodeDetail({id:g.query.nodeId}),D=_.data;D||console.log("\u6587\u6863\u52A0\u8F7D\u5931\u8D25",_),l.value=D,e.commit("addTableName",{key:g.fullPath,val:D.nodeName});let V=[];O.filter(m=>m.paramType===2).forEach(m=>{V.push({name:m.paramKey,value:m.paramValue,type:"string",key:"g"+P++})}),D.headerData&&JSON.parse(D.headerData).forEach(I=>{V.unshift({name:I.code,value:I.value,type:"string",key:"g"+P++})}),C.value=V;let A=[];O.filter(m=>m.paramType===3).forEach(m=>{A.push({name:m.paramKey,value:m.paramValue,type:"string",key:"g"+P++})}),D.cookieData&&JSON.parse(D.cookieData).forEach(I=>{A.unshift({name:I.code,value:I.value,type:"string",key:"g"+P++})}),w.value=A;let F=[];O.filter(m=>m.paramType===1).forEach(m=>{F.push({name:m.paramKey,value:m.paramValue,type:"string",key:"g"+P++})}),b.value=F});const de=()=>{W(),X.apiCustomNodeAdd(l.value).then(_=>{ae.success("\u4FDD\u5B58\u6210\u529F"),Y()})},W=()=>{l.value.nodeName||(l.value.nodeName="\u65B0\u5EFA\u63A5\u53E3")};return{activePage:o,activePageChange:ie,requestLoading:K,sendRequest:ne,saveCustomRequest:de,requestResult:G,consumesParamType:H,urlParamRef:L,urlParamList:u,headerParamRef:c,headerParamList:C,cookieParamRef:v,cookieParamList:w,formParamRef:R,formParamList:b,formEncodeParamRef:d,formEncodeParamList:S,bodyParamRef:N,bodyParamType:k,bodyRowParamList:z,queryParamVisible:B,docInfoShow:l,hideQueryParam:re,showQueryParam:se,methodList:["get","post","put","patch","head","delete","options","trace"]}}},Ye={class:"api-name-box"},We=r(" \u4FDD\u5B58"),Ze=r("\u53D1\u9001\u8BF7\u6C42"),$e={style:{"margin-bottom":"6px"}},ea=r("none"),aa=r("form-data"),ta=r("x-www-form-urlencoded"),oa=r("row"),la=r("binary"),na=r("JSON"),ra=r("HTML"),sa=r("XML"),ia=r("JavaScript"),da=r("TEXT"),ua=r("\u6536\u8D77\u53C2\u6570"),ma=r("\u5C55\u5F00\u53C2\u6570");function ca(p,o,g,e,h,O){const P=i("a-input"),L=i("a-col"),u=i("save-outlined"),c=i("a-button"),C=i("a-row"),v=i("a-select-option"),w=i("a-select"),R=i("a-input-search"),b=i("ParamTable"),d=i("a-tab-pane"),S=i("a-radio"),N=i("a-radio-group"),k=i("ParamBody"),H=i("a-tabs"),z=i("ApiRequestResult");return f(),T(Q,null,[y("div",Ye,[a(C,{type:"flex"},{default:t(()=>[a(L,{flex:"auto"},{default:t(()=>[a(P,{value:e.docInfoShow.nodeName,"onUpdate:value":o[0]||(o[0]=l=>e.docInfoShow.nodeName=l),bordered:!1,placeholder:"\u8BF7\u8F93\u5165\u63A5\u53E3\u540D\u79F0"},null,8,["value"])]),_:1}),a(L,{flex:"88px"},{default:t(()=>[a(c,{onClick:e.saveCustomRequest,type:"dashed"},{default:t(()=>[a(u),We]),_:1},8,["onClick"])]),_:1})]),_:1})]),a(R,{value:e.docInfoShow.apiUrl,"onUpdate:value":o[2]||(o[2]=l=>e.docInfoShow.apiUrl=l),onSearch:e.sendRequest,placeholder:"\u8BF7\u8F93\u5165\u76EE\u6807URL\u5730\u5740"},{addonBefore:t(()=>[a(w,{value:e.docInfoShow.method,"onUpdate:value":o[1]||(o[1]=l=>e.docInfoShow.method=l),style:{width:"100px"}},{default:t(()=>[(f(!0),T(Q,null,Pe(e.methodList,l=>(f(),q(v,{value:l},{default:t(()=>[r(E(l.toUpperCase()),1)]),_:2},1032,["value"]))),256))]),_:1},8,["value"])]),enterButton:t(()=>[a(c,{type:"primary",loading:e.requestLoading},{default:t(()=>[Ze]),_:1},8,["loading"])]),_:1},8,["value","onSearch"]),a(H,{activeKey:e.activePage,"onUpdate:activeKey":o[5]||(o[5]=l=>e.activePage=l),closable:"",onTabClick:e.activePageChange,style:{padding:"5px 10px 0"}},{rightExtra:t(()=>[e.queryParamVisible?(f(),q(c,{key:0,onClick:e.hideQueryParam,type:"link"},{default:t(()=>[ua]),_:1},8,["onClick"])):(f(),q(c,{key:1,onClick:e.showQueryParam,type:"link"},{default:t(()=>[ma]),_:1},8,["onClick"]))]),default:t(()=>[a(d,{tab:"URL\u53C2\u6570",key:"urlParam",forceRender:""},{default:t(()=>[U(y("div",null,[a(b,{ref:"urlParamRef",paramList:e.urlParamList},null,8,["paramList"])],512),[[j,e.queryParamVisible]])]),_:1}),e.docInfoShow.method!=="get"?(f(),q(d,{tab:"Body\u53C2\u6570",key:"bodyParam",forceRender:""},{default:t(()=>[U(y("div",null,[y("div",$e,[a(N,{value:e.bodyParamType,"onUpdate:value":o[3]||(o[3]=l=>e.bodyParamType=l)},{default:t(()=>[a(S,{value:"none"},{default:t(()=>[ea]),_:1}),a(S,{value:"form"},{default:t(()=>[aa]),_:1}),a(S,{value:"formUrlEncode"},{default:t(()=>[ta]),_:1}),a(S,{value:"row"},{default:t(()=>[oa]),_:1}),a(S,{value:"binary"},{default:t(()=>[la]),_:1})]),_:1},8,["value"]),e.bodyParamType==="row"?(f(),q(w,{key:0,value:e.consumesParamType,"onUpdate:value":o[4]||(o[4]=l=>e.consumesParamType=l),size:"small",style:{"margin-left":"10px","vertical-align":"top",width:"100px"}},{default:t(()=>[a(v,{value:"json"},{default:t(()=>[na]),_:1}),a(v,{value:"html"},{default:t(()=>[ra]),_:1}),a(v,{value:"xml"},{default:t(()=>[sa]),_:1}),a(v,{value:"javascript"},{default:t(()=>[ia]),_:1}),a(v,{value:"text"},{default:t(()=>[da]),_:1})]),_:1},8,["value"])):J("",!0)]),U(y("div",null,[a(b,{ref:"formParamRef",paramList:e.formParamList,showType:""},null,8,["paramList"])],512),[[j,e.bodyParamType==="form"]]),U(y("div",null,[a(b,{ref:"formEncodeParamRef",paramList:e.formEncodeParamList},null,8,["paramList"])],512),[[j,e.bodyParamType==="formUrlEncode"]]),U(y("div",null,[a(k,{ref:"bodyParamRef",rowLang:e.consumesParamType,paramList:e.bodyRowParamList},null,8,["rowLang","paramList"])],512),[[j,e.bodyParamType==="row"]])],512),[[j,e.queryParamVisible]])]),_:1})):J("",!0),a(d,{tab:"Header\u53C2\u6570",key:"headerParam",forceRender:""},{default:t(()=>[U(y("div",null,[a(b,{ref:"headerParamRef",paramList:e.headerParamList},null,8,["paramList"])],512),[[j,e.queryParamVisible]])]),_:1}),a(d,{tab:"Cookie\u53C2\u6570",key:"cookieParam",forceRender:""},{default:t(()=>[U(y("div",null,[a(b,{ref:"cookieParamRef",paramList:e.cookieParamList},null,8,["paramList"])],512),[[j,e.queryParamVisible]])]),_:1})]),_:1},8,["activeKey","onTabClick"]),a(z,{result:e.requestResult,loading:e.requestLoading},null,8,["result","loading"])],64)}var Pa=le(Xe,[["render",ca]]);export{Pa as default};
