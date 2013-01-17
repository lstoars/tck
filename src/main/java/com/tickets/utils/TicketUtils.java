package com.tickets.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tickets.domain.OrderParam;
import com.tickets.domain.Person;
import com.tickets.domain.SeatType;
import com.tickets.domain.Train;

public class TicketUtils {
	
	private static final String stationCode="@bjb|北京北|VAP|0@bjd|北京东|BOP|1@bji|北京|BJP|2@bjn|北京南|VNP|3@bjx|北京西|BXP|4@gju|革居|GEM|5@gmc|光明城|IMQ|6@hme|虎门|IUQ|7@qsh|庆盛|QSQ|8@szb|深圳北|IOQ|9@xyc|西阳村|XQF|10@cqb|重庆北|CUW|11@cqi|重庆|CQW|12@cqn|重庆南|CRW|13@sha|上海|SHH|14@shn|上海南|SNH|15@shq|上海虹桥|AOH|16@shx|上海西|SXH|17@tjb|天津北|TBP|18@tji|天津|TJP|19@tjn|天津南|TIP|20@tjx|天津西|TXP|21@cch|长春|CCT|22@ccn|长春南|CET|23@cdd|成都东|ICW|24@cdu|成都|CDW|25@csg|长沙埂|CSW|26@csh|长沙|CSQ|27@csn|长沙南|CWQ|28@fzh|福州|FZS|29@fzn|福州南|FYS|30@gya|贵阳|GIW|31@gyx|贵阳西|GQW|32@gzb|广州北|GBQ|33@gzd|广州东|GGQ|34@gzh|广州|GZQ|35@gzn|广州南|IZQ|36@heb|哈尔滨|HBB|37@hed|哈尔滨东|VBB|38@hfe|合肥|HFH|39@hfx|合肥西|HTH|40@hhd|呼和浩特东|NDC|41@hht|呼和浩特|HHC|42@hkd|海口东|HMQ|43@hko|海口|VUQ|44@hzh|杭州|HZH|45@hzn|杭州南|XHH|46@jna|济南|JNK|47@jnd|济南东|JAK|48@jnx|济南西|JGK|49@kmi|昆明|KMM|50@kmx|昆明西|KXM|51@lsa|拉萨|LSO|52@lzd|兰州东|LVJ|53@lzh|兰州|LZJ|54@lzx|兰州西|LAJ|55@nch|南昌|NCG|56@nji|南京|NJH|57@njn|南京南|NKH|58@njx|南京西|NIH|59@nni|南宁|NNZ|60@sjb|石家庄北|VVP|61@sjz|石家庄|SJP|62@sya|沈阳|SYT|63@syb|沈阳北|SBT|64@syd|沈阳东|SDT|65@tyb|太原北|TBV|66@tyd|太原东|TDV|67@tyu|太原|TYV|68@wha|武汉|WHN|69@wjx|王家营西|KNM|70@wlq|乌鲁木齐|WMR|71@xab|西安北|EAY|72@xan|西安|XAY|73@xan|西安南|CAY|74@xnx|西宁西|XXO|75@ych|银川|YIJ|76@ycn|银川南|YEJ|77@zzh|郑州|ZZF|78@aes|阿尔山|ART|79@aka|安康|AKY|80@aks|阿克苏|ASR|81@alh|阿里河|AHX|82@alk|阿拉山口|AKR|83@api|安平|APT|84@aqi|安庆|AQH|85@ash|安顺|ASW|86@ash|鞍山|AST|87@aya|安阳|AYF|88@ban|北安|BAB|89@bbu|蚌埠|BBH|90@bch|白城|BCT|91@bha|北海|BHZ|92@bhe|白河|BEL|93@bji|白涧|BAP|94@bji|宝鸡|BJY|95@bji|滨江|BJB|96@bkt|博克图|BKX|97@bse|百色|BIZ|98@bss|白山市|HJL|99@bta|北台|BTT|100@btd|包头东|BDC|101@bto|包头|BTC|102@btz|北屯镇|BXR|103@bxi|本溪|BXT|104@byb|白云鄂博|BEC|105@byx|白银西|BXJ|106@bzh|亳州|BZH|107@cbi|赤壁|CBN|108@cde|常德|VGQ|109@cde|承德|CDP|110@cdi|长甸|CDT|111@cfe|赤峰|CFD|112@cli|茶陵|CDG|113@cna|苍南|CEH|114@cpi|昌平|CPP|115@cre|崇仁|CRG|116@ctu|昌图|CTT|117@ctz|长汀镇|CDB|118@cxi|崇信|CIJ|119@cxi|曹县|CXK|120@cxi|楚雄|COM|121@cxt|陈相屯|CXT|122@czb|长治北|CBF|123@czh|长征|CZJ|124@czh|池州|IYH|125@czh|常州|CZH|126@czh|郴州|CZQ|127@czh|长治|CZF|128@czh|沧州|COP|129@czu|崇左|CZZ|130@dab|大安北|RNT|131@dch|大成|DCT|132@ddo|丹东|DUT|133@dfh|东方红|DFB|134@dgd|东莞东|DMQ|135@dhs|大虎山|DHD|136@dhu|敦煌|DHJ|137@dhu|敦化|DHL|138@dhu|德惠|DHT|139@djc|东京城|DJB|140@dji|大涧|DFP|141@djy|都江堰|DDW|142@dli|大理|DKM|143@dli|大连|DLT|144@dna|定南|DNG|145@dqi|大庆|DZX|146@dsh|东胜|DOC|147@dsq|大石桥|DQT|148@dto|大同|DTV|149@dyi|东营|DPK|150@dys|大杨树|DUX|151@dyu|都匀|RYW|152@dzh|邓州|DOF|153@dzh|达州|RXW|154@dzh|德州|DZP|155@ejn|额济纳|EJC|156@eli|二连|RLC|157@esh|恩施|ESN|158@fcg|防城港|FEZ|159@fdi|福鼎|FES|160@fld|风陵渡|FLV|161@fli|涪陵|FLW|162@flj|富拉尔基|FRX|163@fsb|抚顺北|FET|164@fsh|佛山|FSQ|165@fxi|阜新|FXD|166@fya|阜阳|FYH|167@gem|格尔木|GRO|168@gha|广汉|GHW|169@gji|古交|GJV|170@glb|桂林北|GBZ|171@gli|古莲|GRX|172@gli|桂林|GLZ|173@gqi|高崎|XBS|174@gsh|固始|GXN|175@gsh|广水|GSN|176@gta|干塘|GNJ|177@gyu|广元|GYW|178@gzh|赣州|GZG|179@gzl|公主岭|GLT|180@han|淮安|AUH|181@hbe|鹤北|HMB|182@hbe|淮北|HRH|183@hbi|淮滨|HVN|184@hbi|河边|HBV|185@hch|潢川|KCN|186@hch|韩城|HCY|187@hda|邯郸|HDP|188@hdz|横道河子|HDB|189@hga|鹤岗|HGB|190@hgt|皇姑屯|HTT|191@hgu|红果|HEM|192@hhe|黑河|HJB|193@hhu|怀化|HHQ|194@hko|汉口|HKN|195@hld|葫芦岛|HLD|196@hle|海拉尔|HRX|197@hll|霍林郭勒|HWD|198@hlu|海伦|HLB|199@hma|侯马|HMV|200@hmi|哈密|HMR|201@hna|淮南|HAH|202@hna|桦南|HNB|203@hnx|海宁西|EUH|204@hrb|怀柔北|HBP|205@hro|怀柔|HRP|206@hsd|黄石东|OSN|207@hsh|华山|HSY|208@hsh|黄石|HSN|209@hsh|黄山|HKH|210@hsh|衡水|HSP|211@hya|衡阳|HYQ|212@hze|菏泽|HIK|213@hzh|贺州|HXZ|214@hzh|汉中|HOY|215@hzh|惠州|HCQ|216@jan|吉安|VAG|217@jan|集安|JAL|218@jbc|江边村|JBG|219@jch|晋城|JCF|220@jcj|金城江|JJZ|221@jdz|景德镇|JCG|222@jfe|嘉峰|JFF|223@jgq|加格达奇|JGX|224@jgs|井冈山|JGG|225@jhe|蛟河|JHL|226@jhn|金华南|RNH|227@jhx|金华西|JBH|228@jji|九江|JJG|229@jli|吉林|JLL|230@jlo|九龙|JQO|231@jme|荆门|JMN|232@jms|佳木斯|JMB|233@jni|济宁|JIK|234@jnn|集宁南|JAC|235@jqu|酒泉|JQJ|236@jsh|江山|JUH|237@jsh|吉首|JIQ|238@jta|九台|JTL|239@jts|镜铁山|JVJ|240@jxi|鸡西|JXB|241@jxi|蓟县|JKP|242@jxx|绩溪县|JRH|243@jyg|嘉峪关|JGJ|244@jyo|江油|JFW|245@jzh|锦州|JZD|246@jzh|金州|JZT|247@kel|库尔勒|KLR|248@kfe|开封|KFF|249@kla|岢岚|KLV|250@kli|凯里|KLW|251@ksh|喀什|KSR|252@ksn|昆山南|KNH|253@ktu|奎屯|KTR|254@kyu|开原|KYT|255@lan|六安|UAH|256@lba|灵宝|LBF|257@lcg|芦潮港|UCH|258@lch|隆昌|LCW|259@lch|陆川|LKZ|260@lch|利川|LCN|261@lch|临川|LCG|262@lch|潞城|UTP|263@lda|鹿道|LDL|264@ldi|娄底|LDQ|265@lfe|临汾|LFV|266@lgz|良各庄|LGP|267@lhe|临河|LHC|268@lhe|漯河|LON|269@lhu|绿化|LWJ|270@lhu|隆化|UHP|271@lji|丽江|LHM|272@lji|临江|LQL|273@lji|龙井|LJL|274@ljy|龙家营|LKP|275@lli|吕梁|LHV|276@lli|醴陵|LLG|277@lpi|滦平|UPP|278@lps|六盘水|UMW|279@lqi|灵丘|LVV|280@lsh|旅顺|LST|281@lxi|陇西|LXJ|282@lxi|澧县|LEQ|283@lxi|兰溪|LWH|284@lxi|临西|UEP|285@lya|耒阳|LYQ|286@lya|洛阳|LYF|287@lya|龙岩|LYS|288@lyd|洛阳东|LDF|289@lyd|连云港东|UKH|290@lyi|临沂|LVK|291@lym|洛阳龙门|LLF|292@lyu|柳园|DHR|293@lyu|凌源|LYD|294@lyu|辽源|LYL|295@lzh|立志|LZX|296@lzh|柳州|LZZ|297@lzh|辽中|LZD|298@mch|麻城|MCN|299@mdh|免渡河|MDX|300@mdj|牡丹江|MDB|301@meg|莫尔道嘎|MRX|302@mgu|满归|MHX|303@mgu|明光|MGH|304@mhe|漠河|MVX|305@mji|梅江|MKQ|306@mmd|茂名东|MDQ|307@mmi|茂名|MMZ|308@msh|密山|MSB|309@msj|马三家|MJT|310@mwe|麻尾|VAW|311@mya|绵阳|MYW|312@mzh|梅州|MOQ|313@mzl|满洲里|MLX|314@nbd|宁波东|NVH|315@nch|南岔|NCB|316@nch|南充|NCW|317@nda|南丹|NDZ|318@ndm|南大庙|NMP|319@nfe|南芬|NFT|320@nhe|讷河|NHX|321@nji|嫩江|NGX|322@nji|内江|NJW|323@npi|南平|NPS|324@nto|南通|NUH|325@nya|南阳|NFF|326@nzs|碾子山|NZX|327@pds|平顶山|PEN|328@pji|盘锦|PVD|329@pli|平凉|PIJ|330@pln|平凉南|POJ|331@pqu|平泉|PQP|332@psh|坪石|PSQ|333@ptu|沛屯|PUA|334@pxi|萍乡|PXG|335@pxi|凭祥|PXZ|336@pxx|郫县西|PCW|337@pzh|攀枝花|PRW|338@qch|蕲春|QRN|339@qcs|青城山|QSW|340@qda|青岛|QDK|341@qhc|清河城|QYP|342@qji|黔江|QNW|343@qji|曲靖|QJM|344@qjz|前进镇|QEB|345@qqe|齐齐哈尔|QHX|346@qth|七台河|QTB|347@qxi|沁县|QVV|348@qzd|泉州东|QRS|349@qzh|泉州|QYS|350@qzh|衢州|QEH|351@ran|融安|RAZ|352@rjg|汝箕沟|RQJ|353@rji|瑞金|RJG|354@rzh|日照|RZK|355@scp|双城堡|SCB|356@sfh|绥芬河|SFB|357@sgd|韶关东|SGQ|358@shg|山海关|SHD|359@shu|绥化|SHB|360@sjf|三间房|SFX|361@sjt|苏家屯|SXT|362@sla|舒兰|SLL|363@smi|三明|SMS|364@smu|神木|OMY|365@smx|三门峡|SMF|366@sna|商南|ONY|367@sni|遂宁|NIW|368@spi|四平|SPT|369@sqi|商丘|SQF|370@sra|上饶|SRG|371@ssh|韶山|SSQ|372@sso|宿松|OAH|373@sto|汕头|OTQ|374@swu|邵武|SWS|375@sxi|涉县|OEP|376@sya|三亚|SEQ|377@sya|邵阳|SYQ|378@sya|十堰|SNN|379@sys|双鸭山|SSB|380@syu|松原|VYT|381@szh|深圳|SZQ|382@szh|苏州|SZH|383@szh|随州|SZN|384@szh|宿州|OXH|385@szh|朔州|SUV|386@szx|深圳西|OSQ|387@tba|塘豹|TBQ|388@teq|塔尔气|TVX|389@tgu|潼关|TGY|390@tgu|塘沽|TGP|391@the|塔河|TXX|392@thu|通化|THL|393@tla|泰来|TLX|394@tlf|吐鲁番|TFR|395@tli|通辽|TLD|396@tli|铁岭|TLT|397@tlz|陶赖昭|TPT|398@tme|图们|TML|399@tre|铜仁|RDQ|400@tsb|唐山北|FUP|401@tsf|田师府|TFT|402@tsh|泰山|TAK|403@tsh|天水|TSJ|404@tsh|唐山|TSP|405@typ|通远堡|TYT|406@tys|太阳升|TQT|407@tzh|泰州|UTH|408@tzi|桐梓|TZW|409@tzx|通州西|TAP|410@wch|五常|WCB|411@wch|武昌|WCN|412@wfd|瓦房店|WDT|413@whi|威海|WKK|414@whu|芜湖|WHH|415@whx|乌海西|WXC|416@wjt|吴家屯|WJT|417@wlo|武隆|WLW|418@wlt|乌兰浩特|WWT|419@wna|渭南|WNY|420@wsh|威舍|WSM|421@wts|歪头山|WIT|422@wwe|武威|WUJ|423@wwn|武威南|WWJ|424@wxi|无锡|WXH|425@wxi|乌西|WXR|426@wyl|乌伊岭|WPB|427@wys|武夷山|WAS|428@wyu|万源|WYY|429@wzh|万州|WYW|430@wzh|梧州|WZZ|431@wzh|温州|RZH|432@wzn|温州南|VRH|433@xch|西昌|ECW|434@xch|许昌|XCF|435@xcn|西昌南|ENW|436@xfa|香坊|XFB|437@xga|轩岗|XGV|438@xgu|兴国|EUG|439@xha|宣汉|XHY|440@xhu|新会|EFQ|441@xhu|新晃|XLQ|442@xlt|锡林浩特|XTC|443@xlx|兴隆县|EXP|444@xmb|厦门北|XKS|445@xme|厦门|XMS|446@xnh|小南海|XHW|447@xsh|秀山|ETW|448@xsh|小市|XST|449@xta|向塘|XTG|450@xwe|宣威|XWM|451@xxi|新乡|XXF|452@xya|信阳|XUN|453@xya|咸阳|XYY|454@xya|襄阳|XFN|455@xyc|熊岳城|XYT|456@xyi|兴义|XRZ|457@xyi|新沂|VIH|458@xyu|新余|XUG|459@xzh|徐州|XCH|460@yan|延安|YWY|461@ybi|宜宾|YBW|462@ybn|亚布力南|YWB|463@ybs|叶柏寿|YBD|464@ycd|宜昌东|HAN|465@ych|永川|YCW|466@ych|宜春|YCG|467@ych|宜昌|YCN|468@ych|盐城|AFH|469@ych|运城|YNV|470@ych|伊春|YCB|471@yci|榆次|YCV|472@ycu|杨村|YBP|473@yga|燕岗|YGW|474@yji|永济|YIV|475@yji|延吉|YJL|476@yko|营口|YKT|477@yks|牙克石|YKX|478@yli|阎良|YNY|479@yli|玉林|YLZ|480@yli|榆林|ALY|481@ymp|一面坡|YPB|482@yni|伊宁|YMR|483@ypg|阳平关|YAY|484@ypi|玉屏|YZW|485@ypi|原平|YPV|486@yqi|延庆|YNP|487@yqq|阳泉曲|YYV|488@yqu|玉泉|YQB|489@yqu|阳泉|AQP|490@ysh|营山|NUW|491@ysh|玉山|YNG|492@ysh|燕山|AOP|493@ysh|榆树|YRT|494@yta|鹰潭|YTG|495@yta|烟台|YAK|496@yth|伊图里河|YEX|497@ytx|玉田县|ATP|498@ywu|义乌|YWH|499@yxi|阳新|YON|500@yxi|义县|YXD|501@yya|益阳|AEQ|502@yya|岳阳|YYQ|503@yzh|永州|AOQ|504@yzh|扬州|YLH|505@zbo|淄博|ZBK|506@zcd|镇城底|ZDV|507@zgo|自贡|ZGW|508@zhb|珠海北|ZIQ|509@zji|湛江|ZJZ|510@zji|镇江|ZJH|511@zjj|张家界|DIQ|512@zjk|张家口|ZKP|513@zjn|张家口南|ZMP|514@zko|周口|ZKN|515@zlm|哲里木|ZLC|516@zlt|扎兰屯|ZTX|517@zmd|驻马店|ZDN|518@zqi|肇庆|ZVQ|519@zsz|周水子|ZIT|520@zto|昭通|ZDW|521@zwe|中卫|ZWJ|522@zya|资阳|ZYW|523@zyi|遵义|ZIW|524@zzh|枣庄|ZEK|525@zzh|资中|ZZW|526@zzh|株洲|ZZQ|527@zzx|枣庄西|ZFK|528@aax|昂昂溪|AAX|529@abg|艾不盖|ABC|530@ach|阿城|ACB|531@ada|安达|ADX|532@ada|埃岱|ADW|533@adi|安定|ADP|534@afz|安富镇|AZW|535@agt|阿贵图|AGC|536@agu|安广|AGT|537@ahe|艾河|AHP|538@ahu|安化|PKQ|539@ajc|艾家村|AJJ|540@aji|鳌江|ARH|541@aji|安家|AJB|542@aji|阿金|AJD|543@akt|阿克陶|AER|544@aky|安口窑|AYY|545@alg|敖力布告|ALD|546@alo|安龙|AUZ|547@als|阿龙山|ASX|548@alu|安陆|ALN|549@ame|阿木尔|JTX|550@anz|阿南庄|AZM|551@aqx|安庆西|APH|552@ata|安塘|ATV|553@atb|安亭北|ASH|554@ats|阿图什|ATR|555@atu|安图|ATL|556@axi|安溪|AXS|557@azh|阿寨|AAW|558@bao|博鳌|BWQ|559@bbg|白壁关|BGV|560@bbn|蚌埠南|BMH|561@bch|巴楚|BCR|562@bch|板城|BUP|563@bcu|柏村|BCW|564@bdh|北戴河|BEP|565@bdi|保定|BDP|566@bdi|宝坻|BPP|567@bdl|八达岭|ILP|568@bdo|巴东|BNN|569@bgu|柏果|BGM|570@bgu|丙谷|BVW|571@bgu|白果|BIW|572@bhd|白河东|BIY|573@bhe|布海|BUT|574@bho|贲红|BVC|575@bhs|宝华山|BWH|576@bhx|白河县|BEY|577@bjg|白芨沟|BJJ|578@bjg|碧鸡关|BJM|579@bji|北滘|IBQ|580@bji|保健|BPC|581@bji|碧江|BLQ|582@bjl|保家楼|BJW|583@bjp|白鸡坡|BBM|584@bjs|笔架山|BSB|585@bjt|八角台|BTD|586@bka|保康|BKD|587@bkp|白奎堡|BKB|588@bkq|毕克齐|BKC|589@bla|白狼|BAT|590@bla|百浪|BRZ|591@ble|博乐|BOR|592@blg|宝拉格|BQC|593@bli|巴林|BLX|594@bli|宝林|BNB|595@bli|柏林|BLW|596@bli|北流|BOZ|597@bli|勃利|BLB|598@bli|坝梁|BLC|599@blk|布列开|BLR|600@bls|宝老山|BRW|601@bls|宝龙山|BND|602@blx|百里峡|GNP|603@bma|白马|KMW|604@bmc|八面城|BMD|605@bmq|班猫箐|BNM|606@bmt|八面通|BMB|607@bmt|宝木吐|BMC|608@bmz|木镇|BZW|609@bmz|北马圈子|BRP|610@bpn|北票南|RPD|611@bqi|白旗|BQP|612@bql|宝泉岭|BQB|613@bsh|白沙|BSW|614@bsh|巴山|BAY|615@bsj|白水江|BSY|616@bsm|八苏木|BSC|617@bsp|白沙坡|BPM|618@bss|白石山|BAL|619@bst|白沙沱|BXW|620@bsy|白石岩|BOW|621@bsz|白水镇|BUM|622@bta|白涛|BNW|623@btb|包头北|BBC|624@bti|坂田|BTQ|625@bto|泊头|BZP|626@btu|北屯|BYP|627@btx|包头西|BXC|628@bxi|博兴|BXK|629@bxt|八仙筒|VXD|630@bye|白银哈尔|BIC|631@byg|白音察干|BYC|632@byh|背荫河|BYB|633@byh|白彦花|BHC|634@byi|北营|BIV|635@byl|巴彦郭勒|BGC|636@byl|巴彦高勒|BAC|637@byl|白音他拉|BID|638@byl|白音特拉|BRC|639@bys|白银市|BNJ|640@bys|白音胡硕|BCD|641@bzh|霸州|RMP|642@bzh|北宅|BVP|643@cba|茨坝|CBW|644@cbb|赤壁北|CIN|645@cbg|查布嘎|CBC|646@cch|长城|CEJ|647@cch|长冲|CCM|648@cch|茨冲|CCW|649@cdd|承德东|CCP|650@cfx|赤峰西|CID|651@cga|嵯岗|CAX|652@cgd|查干哈达|CDC|653@cge|长葛|CEF|654@cge|朝格温多尔|CWC|655@cgg|查干特格|CTC|656@cgh|查干芒和|CMC|657@cgp|柴沟堡|CGV|658@cgu|城固|CGY|659@cgy|陈官营|CAJ|660@cgz|成高子|CZB|661@cha|草海|WBW|662@cha|厂汗|CHC|663@chb|长河碥|CHW|664@chb|长河坝|CAW|665@che|柴河|CHB|666@che|册亨|CHZ|667@che|岔河|CKW|668@chk|草河口|CKT|669@chk|崔黄口|CHP|670@chu|巢湖|CIH|671@cjg|蔡家沟|CJT|672@cjh|成吉思汗|CJX|673@cji|岔江|CAM|674@cjp|蔡家坡|CJY|675@cjw|陈家湾|CWW|676@cko|沧口|CKK|677@cle|昌乐|CLK|678@clg|超梁沟|CYP|679@cli|慈利|CUQ|680@cli|昌黎|CLP|681@clt|楚鲁图|CLC|682@clz|长岭子|CLT|683@cmi|晨明|CMB|684@cno|长农|CNJ|685@cpb|昌平北|VBP|686@cpl|长坡岭|CPM|687@cqi|辰清|CQB|688@csh|楚山|CSB|689@csh|长寿|EFW|690@csh|磁山|CSP|691@csh|苍石|CST|692@csh|草市|CSL|693@csh|蔡山|CON|694@csq|察素齐|CSC|695@cst|长山屯|CVT|696@ctg|长潭沟|FCW|697@cti|长汀|CES|698@cwa|春湾|CQQ|699@cxi|磁县|CIP|700@cxi|岑溪|CNZ|701@cxi|辰溪|CXQ|702@cxi|长兴|CFH|703@cxi|磁西|CRP|704@cya|磁窑|CYK|705@cya|朝阳|CYD|706@cya|春阳|CAL|707@cya|城阳|CEK|708@cyc|创业村|CEX|709@cyc|朝阳川|CYL|710@cyd|朝阳地|CDD|711@cyu|长垣|CYF|712@cyz|朝阳镇|CZL|713@czb|滁州北|CUH|714@czb|常州北|ESH|715@czh|滁州|CXH|716@czh|潮州|CKQ|717@czh|崔寨|CZI|718@czh|常庄|CVK|719@czl|曹子里|CFP|720@czw|车转湾|CWM|721@czx|郴州西|ICQ|722@czx|沧州西|CBP|723@dan|德安|DAG|724@dan|东安|DAZ|725@dba|大坝|DBJ|726@dba|大板|DBC|727@dba|大巴|DBD|728@dba|到保|RBT|729@dbi|定边|DYJ|730@dbj|东边井|DBB|731@dbs|德伯斯|RDT|732@dcg|打柴沟|DGJ|733@dch|德昌|DVW|734@dda|滴道|DDB|735@dde|大德|DEM|736@dde|道德|DCC|737@ddg|大磴沟|DKJ|738@ded|刀尔登|DRD|739@dee|得耳布尔|DRX|740@dfa|东方|UFQ|741@dfe|丹凤|DGY|742@dfe|东丰|DIL|743@dga|达盖|DAC|744@dgt|大官屯|DTT|745@dgu|大关|RGW|746@dgu|东光|DGP|747@dgu|东莞|DAQ|748@dha|东海|DHB|749@dhb|大河坝|DIW|750@dhc|大灰厂|DHP|751@dhq|大红旗|DQD|752@dhx|东海县|DQH|753@djc|邓家村|DJW|754@djd|刁家段|DDC|755@djg|达家沟|DJT|756@dji|东津|DKB|757@dji|杜家|DJL|758@djw|邓家湾|REW|759@djz|大旧庄|DJM|760@dkt|大口屯|DKP|761@dla|东来|RVD|762@dlh|德令哈|DHO|763@dlh|大陆号|DLC|764@dli|带岭|DLB|765@dli|大林|DLD|766@dll|道仑郭勒|DEC|767@dlq|达拉特旗|DIC|768@dlt|独立屯|DTX|769@dlx|达拉特西|DNC|770@dmc|东明村|DMD|771@dmh|洞庙河|DEP|772@dmx|东明县|DNF|773@dni|大拟|DNZ|774@dpf|大平房|DPD|775@dps|大盘石|RPP|776@dpu|大埔|DPI|777@dpu|大堡|DVT|778@dqh|大其拉哈|DQX|779@dqi|德清|MOH|780@dqi|道清|DML|781@dqs|对青山|DQB|782@drg|达日其嘎|DQC|783@drt|德日斯图|DRC|784@dsb|东升坝|DAW|785@dsh|东升|DRQ|786@dsh|独山|RWW|787@dsh|渡市|RUW|788@dsh|砀山|DKH|789@dsh|登沙河|DWT|790@dsp|读书铺|DPM|791@dsp|大山铺|DSW|792@dst|大石头|DSL|793@dsz|大石寨|RZT|794@dta|东台|DBH|795@dta|定陶|DQK|796@dta|灯塔|DGT|797@dtb|大田边|DBM|798@dth|东通化|DTL|799@dtu|丹徒|RUH|800@dtu|大屯|DNT|801@dwa|东湾|DRJ|802@dwa|代湾|DWW|803@dwk|大武口|DFJ|804@dwp|低窝铺|DWJ|805@dwt|大王滩|DZZ|806@dwz|大湾子|DFM|807@dxg|大兴沟|DXL|808@dxi|大兴|DXX|809@dxi|定西|DSJ|810@dxi|甸心|DXM|811@dxi|东乡|DXG|812@dxi|代县|DKV|813@dxi|定襄|DXV|814@dxi|东兴|DXC|815@dxu|东戌|RXP|816@dxz|东辛庄|DXD|817@dya|丹阳|DYH|818@dya|大雁|DYX|819@dya|德阳|DYW|820@dya|当阳|DYN|821@dya|打羊|RNW|822@dyb|丹阳北|EXH|823@dyd|大英东|IAW|824@dyd|东淤地|DBV|825@dyi|大营|DYV|826@dyu|定远|EWH|827@dyu|岱岳|RYV|828@dyu|大元|DYZ|829@dyy|登瀛崖|DNW|830@dyz|大营镇|DJP|831@dyz|大营子|DZD|832@dzc|大战场|DTJ|833@dzd|德州东|DIP|834@dzh|低庄|DVQ|835@dzh|东镇|DNV|836@dzh|道州|DFZ|837@dzh|东至|DCH|838@dzh|兑镇|DWV|839@dzh|豆庄|ROP|840@dzh|定州|DXP|841@dzu|大足|RIW|842@dzy|大竹园|DZY|843@dzz|大杖子|DAP|844@dzz|豆张庄|RZP|845@ebi|峨边|EBW|846@edg|二道沟|RGC|847@edm|二道沟门|RDP|848@edw|二道湾|RDX|849@edy|二道岩|RDW|850@elo|二龙|RLD|851@elt|二龙山屯|ELA|852@eme|峨眉|EMW|853@esh|尔赛河|RSW|854@eyi|二营|RYJ|855@ezh|鄂州|ECN|856@fan|福安|FAS|857@fch|防城|FAZ|858@fch|丰城|FCG|859@fcn|丰城南|FNG|860@fdo|峰洞|FDW|861@fdo|肥东|FIH|862@fer|发耳|FEM|863@fgp|峰高铺|FPW|864@fha|富海|FHX|865@fha|福海|FHR|866@fhc|凤凰城|FHT|867@fhu|奉化|FHH|868@fji|富锦|FIB|869@fjt|范家屯|FTT|870@fju|福巨|FJC|871@flt|福利屯|FTB|872@flz|丰乐镇|FZB|873@fna|阜南|FNH|874@fni|阜宁|AKH|875@fni|抚宁|FNP|876@fqi|福清|FQS|877@fqu|福泉|VMW|878@fsc|丰水村|FSJ|879@fsh|丰顺|FUQ|880@fsh|繁峙|FSV|881@fsh|抚顺|FST|882@fsk|福山口|FKP|883@fsu|扶绥|FSZ|884@fsz|福生庄|FSC|885@ftu|冯屯|FTX|886@fty|浮图峪|FYP|887@fxd|富县东|FDY|888@fxd|福兴地|FXC|889@fxi|凤县|FXY|890@fxi|富县|FEY|891@fxi|费县|FXK|892@fya|凤阳|FUH|893@fya|汾阳|FAV|894@fyi|分宜|FYG|895@fyu|富源|FYM|896@fyu|扶余|FYT|897@fyu|富裕|FYX|898@fzb|抚州北|FBG|899@fzh|凤州|FZY|900@fzh|丰镇|FZC|901@fzh|范镇|VZK|902@gan|固安|GFP|903@gan|广安|VJW|904@gbd|高碑店|GBP|905@gbz|沟帮子|GBD|906@gcb|关村坝|GMW|907@gcd|甘草店|GDJ|908@gch|谷城|GCN|909@gch|藁城|GEP|910@gcw|古城湾|GCC|911@gcz|古城镇|GZB|912@gde|广德|GRH|913@gdi|贵定|GTW|914@gdn|贵定南|IDW|915@gdo|古东|GDV|916@gfe|高峰|GFW|917@gga|贵港|GGZ|918@gga|官高|GVP|919@ggm|葛根庙|GGT|920@ggu|甘谷|GGJ|921@ggu|高谷|FGW|922@ggz|高各庄|GGP|923@ghe|甘河|GAX|924@ghe|共和|GWW|925@ghe|根河|GEX|926@gjb|甘家坝|RAW|927@gjb|公积坂|GJC|928@gjb|姑家堡|GPC|929@gjc|高家村|GAC|930@gjd|郭家店|GDT|931@gjt|古家沱|GJW|932@gjz|孤家子|GKT|933@gla|高老|GOB|934@gla|古浪|GLJ|935@gla|皋兰|GEJ|936@glf|高楼房|GFM|937@glh|古鲁满汗|GHC|938@glh|归流河|GHT|939@gli|关林|GLF|940@glt|嘎拉德斯汰|GLC|941@glu|甘洛|VOW|942@glz|高炉子|VZW|943@glz|郭磊庄|GLP|944@gmi|高密|GMK|945@gmz|公庙子|GMC|946@gnh|工农湖|GRT|947@gns|广宁寺|GNT|948@gnw|广南卫|GNM|949@gpi|高平|GPF|950@gpp|高坪铺|GPW|951@gqb|甘泉北|GEY|952@gqc|共青城|GAG|953@gqk|甘旗卡|GQD|954@gqu|甘泉|GQY|955@gqz|高桥镇|GZD|956@grb|郭尔奔敖包|GRC|957@gsc|广顺场|GCW|958@gsh|赶水|GSW|959@gsh|孤山|GSC|960@gsh|灌水|GST|961@gsk|孤山口|GSP|962@gso|果松|GSL|963@gsz|嘎什甸子|GXD|964@gsz|高山子|GSD|965@gta|高台|GTJ|966@gta|高滩|GAY|967@gti|古田|GTS|968@gti|官厅|GTP|969@gto|广通|GOM|970@gtx|官厅西|KEP|971@gxg|干溪沟|KXW|972@gxi|泔溪|VDW|973@gxi|贵溪|GXG|974@gxi|公兴|GUW|975@gya|涡阳|GYH|976@gyi|巩义|GXF|977@gyi|高邑|GIP|978@gyn|巩义南|GYF|979@gyu|固原|GUJ|980@gyu|菇园|GYL|981@gyz|公营子|GYD|982@gze|光泽|GZS|983@gzg|公主埂|GZC|984@gzh|古镇|GNQ|985@gzh|瓜州|GZJ|986@gzh|固镇|GEH|987@gzh|盖州|GXT|988@gzh|关寨|GRW|989@gzj|官字井|GOT|990@gzp|革镇堡|GZT|991@gzs|冠豸山|GSS|992@han|红安|HWN|993@han|淮安南|AMH|994@hax|红安西|VXN|995@hax|海安县|HIH|996@hax|洪安乡|HAW|997@hba|黄柏|HBL|998@hbe|海北|HEB|999@hbi|鹤壁|HAF|1000@hch|华城|VCQ|1001@hch|合川|WKW|1002@hch|河唇|HCZ|1003@hch|海城|HCT|1004@hch|化处|HEW|1005@hch|湖潮|HCW|1006@hct|黑冲滩|HCJ|1007@hcu|黄村|HCP|1008@hde|化德|HGC|1009@hdo|洪洞|HDV|1010@hfe|横峰|HFG|1011@hfe|红峰|HFW|1012@hfw|韩府湾|HXJ|1013@hgu|汉沽|HGP|1014@hgy|黄瓜园|HYM|1015@hgz|红光镇|IGW|1016@hhg|红花沟|VHD|1017@hht|黄花筒|HUD|1018@hht|红花塘|HHW|1019@hhy|红花园|HYW|1020@hjb|黄家坝|HKW|1021@hjd|贺家店|HJJ|1022@hji|和静|HJR|1023@hji|黑井|HIM|1024@hji|横江|RJW|1025@hji|获嘉|HJF|1026@hji|河津|HJV|1027@hji|涵江|HJS|1028@hji|红江|HFM|1029@hjp|侯家坪|HJW|1030@hjq|杭锦旗|HJC|1031@hjt|忽吉图|HEC|1032@hjx|河间西|HXP|1033@hjz|花家庄|HJM|1034@hkn|河口南|HKJ|1035@hko|黄口|KOH|1036@hko|湖口|HKG|1037@hla|呼兰|HUB|1038@hlb|葫芦岛北|HPD|1039@hlg|黄联关|HGW|1040@hlh|浩良河|HHB|1041@hlh|哈拉海|HIT|1042@hli|鹤立|HOB|1043@hli|桦林|HIB|1044@hli|黄陵|ULY|1045@hli|海林|HRB|1046@hli|虎林|VLB|1047@hli|寒岭|HAT|1048@hlk|好鲁库|HKC|1049@hlo|和龙|HLL|1050@hlo|海龙|HIL|1051@hls|哈拉苏|HAX|1052@hlt|呼鲁斯太|VTJ|1053@hme|黄梅|VEH|1054@hml|荷马岭|HMW|1055@hmt|蛤蟆塘|HMT|1056@hmy|韩麻营|HYP|1057@hnh|黄泥河|HHL|1058@hni|海宁|HNH|1059@hno|惠农|HMJ|1060@hpi|黄平|VFW|1061@hpi|和平|VAQ|1062@hpz|花棚子|HZM|1063@hqi|花桥|VQH|1064@hqi|宏庆|HEY|1065@hqi|黄|HQW|1066@hre|怀仁|HRV|1067@hro|华容|HRN|1068@hrt|贺日斯台|HMC|1069@hsb|华山北|HDY|1070@hsb|红砂坝|HSC|1071@hsd|黄松甸|HDL|1072@hsg|和什托洛盖|VSR|1073@hsh|红山|VSB|1074@hsh|汉寿|VSQ|1075@hsh|衡山|HSQ|1076@hsh|黑水|HOT|1077@hsh|惠山|VCH|1078@hsh|虎什哈|HHP|1079@hsh|猴山|HEP|1080@hsi|黄丝|HRW|1081@hsl|汗苏鲁|HQC|1082@hsp|红寺堡|HSJ|1083@hst|黄水塘|HSW|1084@hsw|海石湾|HSO|1085@hsx|衡山西|HEQ|1086@hsx|红砂岘|VSJ|1087@hta|黑台|HQB|1088@hta|桓台|VTK|1089@hth|汇塘河|HNW|1090@hti|和田|VTR|1091@hto|会同|VTQ|1092@hto|黄桶|HWW|1093@hty|核桃园|KHW|1094@htz|海坨子|HZT|1095@hwa|黑旺|HWK|1096@hwa|海湾|RWH|1097@hxi|红星|VXB|1098@hxi|徽县|HYY|1099@hxl|红兴隆|VHB|1100@hxt|换新天|VTB|1101@hxt|红岘台|HTJ|1102@hya|红彦|VIX|1103@hya|合阳|HAY|1104@hya|海阳|HYK|1105@hya|河阳|IOW|1106@hyd|衡阳东|HVQ|1107@hyi|华蓥|HUW|1108@hyi|汉阴|HQY|1109@hyt|哈业胡同|HTC|1110@hyt|黄羊滩|HGJ|1111@hyu|汉源|WHW|1112@hyu|河源|VIQ|1113@hyu|花园|HUN|1114@hyw|黄羊湾|HWJ|1115@hyz|黄羊镇|HYJ|1116@hzh|化州|HZZ|1117@hzh|黄州|VON|1118@hzh|霍州|HZV|1119@hzx|惠州西|VXQ|1120@jba|巨宝|JRT|1121@jbi|靖边|JIY|1122@jbt|金宝屯|JBD|1123@jcb|晋城北|JEF|1124@jch|金昌|JCJ|1125@jch|鄄城|JCK|1126@jch|交城|JNV|1127@jch|建昌|JFD|1128@jde|峻德|JDB|1129@jdi|井店|JFP|1130@jdo|鸡东|JOB|1131@jdu|江都|UDH|1132@jgs|鸡冠山|JST|1133@jgt|金刚沱|JGW|1134@jgt|金沟屯|VGP|1135@jgu|碱柜|JGC|1136@jha|静海|JHP|1137@jhe|金河|JHX|1138@jhe|锦河|JHB|1139@jhe|锦和|JHQ|1140@jhe|精河|JHR|1141@jhn|精河南|JIR|1142@jhu|江华|JHZ|1143@jhu|建湖|AJH|1144@jjc|金鸡村|JCZ|1145@jjd|金家店|JJC|1146@jjg|纪家沟|VJD|1147@jji|晋江|JJS|1148@jji|江津|JJW|1149@jji|姜家|JJB|1150@jji|经久|JNW|1151@jke|金坑|JKT|1152@jkh|金口河|JHW|1153@jla|加劳|JAW|1154@jli|芨岭|JLJ|1155@jli|九里|JEW|1156@jlt|九龙塘|JTW|1157@jmc|金马村|JMM|1158@jme|江门|JWQ|1159@jna|莒南|JOK|1160@jna|井南|JNP|1161@jou|建瓯|JVS|1162@jpe|经棚|JPC|1163@jqi|江桥|JQX|1164@jsa|九三|SSX|1165@jsb|金山北|EGH|1166@jsh|京山|JCN|1167@jsh|建始|JRN|1168@jsh|嘉善|JSH|1169@jsh|稷山|JVV|1170@jsh|吉舒|JSL|1171@jsh|建设|JET|1172@jsh|甲山|JOP|1173@jsj|建三江|JIB|1174@jsn|嘉善南|EAH|1175@jst|金山屯|JTB|1176@jst|江所田|JOM|1177@jta|景泰|JTJ|1178@jwe|吉文|JWX|1179@jxc|江西村|JAZ|1180@jxi|蕉溪|JXW|1181@jxi|进贤|JUG|1182@jxi|莒县|JKK|1183@jxi|嘉祥|JUK|1184@jxi|介休|JXV|1185@jxi|井陉|JJP|1186@jxi|嘉兴|JXH|1187@jxn|嘉兴南|EPH|1188@jxz|夹心子|JXT|1189@jya|简阳|JYW|1190@jya|揭阳|JRQ|1191@jya|建阳|JYS|1192@jya|姜堰|UEH|1193@jye|巨野|JYK|1194@jyo|江永|JYZ|1195@jyu|靖远|JYJ|1196@jyu|缙云|JYH|1197@jyu|江源|SZL|1198@jyu|济源|JYF|1199@jyx|靖远西|JXJ|1200@jzb|胶州北|JZK|1201@jzc|敬梓场|JCW|1202@jzd|焦作东|WEF|1203@jzh|靖州|JEQ|1204@jzh|金寨|JZH|1205@jzh|晋州|JXP|1206@jzh|胶州|JXK|1207@jzn|锦州南|JOD|1208@jzu|焦作|JOF|1209@jzw|旧庄窝|JVP|1210@jzz|金杖子|JYD|1211@kan|开安|KAT|1212@kch|库车|KCR|1213@kch|康城|KCP|1214@kde|库都尔|KDX|1215@kdi|宽甸|KDT|1216@kdo|克东|KOB|1217@kdz|昆独仑召|KDC|1218@kjj|康金井|KJB|1219@klq|喀喇其|KQX|1220@klu|开鲁|KLC|1221@kly|克拉玛依|KHR|1222@kqi|口前|KQL|1223@ksh|奎山|KAB|1224@ksh|昆山|KSH|1225@ksh|克山|KSB|1226@kta|孔滩|KTW|1227@kto|开通|KTT|1228@kxl|康熙岭|KXZ|1229@kyh|克一河|KHX|1230@kzh|康庄|KZP|1231@lba|滥坝|LQW|1232@lba|拉白|UBW|1233@lbi|来宾|UBZ|1234@lbi|老边|LLT|1235@lbt|龙伯屯|LOZ|1236@lbx|灵宝西|LPF|1237@lbx|临巴溪|UVW|1238@lch|龙池|UPW|1239@lch|龙川|LUQ|1240@lch|乐昌|LCQ|1241@lch|黎城|UCP|1242@lch|聊城|UCK|1243@lcu|蓝村|LCK|1244@ldg|六地沟|LGC|1245@ldo|林东|LRC|1246@ldu|乐都|LDO|1247@ldx|梁底下|LDP|1248@ldz|六道河子|LVP|1249@lfa|鲁番|LVM|1250@lfa|廊坊|LJP|1251@lfa|落垡|LOP|1252@lfb|廊坊北|LFP|1253@lfe|禄丰|LFM|1254@lfu|老府|UFD|1255@lfy|凉风垭|LRW|1256@lga|兰岗|LNB|1257@lgc|老锅厂|KGW|1258@lgd|龙骨甸|LGM|1259@lgj|六个鸡|UOW|1260@lgo|芦沟|LOM|1261@lgo|刘沟|LDW|1262@lgo|龙沟|LGJ|1263@lgt|栏杆滩|LGW|1264@lgu|拉古|LGB|1265@lha|临海|UFH|1266@lha|林海|LXX|1267@lha|拉哈|LHX|1268@lha|凌海|JID|1269@lhe|柳河|LNL|1270@lhe|六合|KLH|1271@lho|凉红|UIW|1272@lhu|龙华|LHP|1273@lhu|珞璜|LHW|1274@lhx|联合乡|UXW|1275@lhy|滦河沿|UNP|1276@lhz|六合镇|LEX|1277@ljc|临江场|LNW|1278@ljc|芦家村|LJC|1279@ljd|亮甲店|LRT|1280@ljd|刘家店|UDT|1281@ljh|刘家河|LVT|1282@lji|连江|LKS|1283@lji|李家|LJB|1284@lji|罗江|LJW|1285@lji|廉江|LJZ|1286@lji|庐江|UJH|1287@lji|两家|UJT|1288@lji|龙江|LJX|1289@lji|刘集|LKI|1290@lji|励家|LID|1291@lji|龙嘉|UJL|1292@ljk|莲江口|LHB|1293@ljl|蔺家楼|ULK|1294@ljp|李家坪|LIJ|1295@ljs|临江寺|LSW|1296@lju|乐居|LVW|1297@ljw|李家湾|LYW|1298@ljz|柳家庄|LKJ|1299@lka|兰考|LKF|1300@lko|林口|LKB|1301@lkp|路口铺|LKQ|1302@lla|老莱|LAX|1303@lli|拉林|LAB|1304@lli|陆良|LRM|1305@lli|龙里|LLW|1306@lli|零陵|UWZ|1307@lli|临澧|LWQ|1308@lli|兰棱|LLB|1309@llo|卢龙|UAP|1310@lmd|喇嘛甸|LMX|1311@lmd|里木店|LMB|1312@lme|洛门|LMJ|1313@lmh|龙门河|MHA|1314@lmu|栗木|LMN|1315@lna|龙南|UNG|1316@lpd|罗盘地|LEW|1317@lpi|梁平|UQW|1318@lpi|罗平|LPM|1319@lpl|落坡岭|LPP|1320@lps|六盘山|UPJ|1321@lps|乐平市|LPG|1322@lqi|临清|UQK|1323@lqs|龙泉寺|UQJ|1324@lsc|乐善村|LUM|1325@lsd|冷水江东|UDQ|1326@lsg|连山关|LGT|1327@lsg|娄山关|UKW|1328@lsg|流水沟|USP|1329@lsh|陵水|LIQ|1330@lsh|蔺市|LWW|1331@lsh|乐山|UTW|1332@lsh|罗山|LRN|1333@lsh|鲁山|LAF|1334@lsh|丽水|USH|1335@lsh|梁山|LMK|1336@lsh|灵石|LSV|1337@lsh|露水河|LUL|1338@lsh|庐山|LSG|1339@lsj|凉水井|FSW|1340@lsp|林盛堡|LBT|1341@lst|柳树屯|LSD|1342@lsz|梨树镇|LSB|1343@lsz|李市镇|LZW|1344@lsz|李石寨|LET|1345@lta|黎塘|LTZ|1346@lta|轮台|LAR|1347@lta|芦台|LTP|1348@ltb|龙塘坝|LBM|1349@ltu|濑湍|LVZ|1350@ltx|骆驼巷|LTJ|1351@lwa|李旺|VLJ|1352@lwd|老王洞|UEW|1353@lwd|莱芜东|LWK|1354@lwh|刘伟壕|LWC|1355@lws|狼尾山|LRJ|1356@lwu|乐武|UWW|1357@lwu|灵武|LNJ|1358@lwx|莱芜西|UXK|1359@lxi|朗乡|LXB|1360@lxi|陇县|LXY|1361@lxi|临湘|LXQ|1362@lxi|莱西|LXK|1363@lxi|林西|LXC|1364@lxi|滦县|UXP|1365@lxm|灵仙庙|LXW|1366@lya|略阳|LYY|1367@lya|莱阳|LYK|1368@lya|辽阳|LYT|1369@lyb|临沂北|UYK|1370@lyd|凌源东|LDD|1371@lyg|连云港|UIH|1372@lyh|老羊壕|LYC|1373@lyi|临颍|LNF|1374@lyi|老营|LXL|1375@lyo|龙游|LMH|1376@lyu|罗源|LVS|1377@lyu|林源|LYX|1378@lyu|涟源|LAQ|1379@lyu|涞源|LYP|1380@lyu|乐跃|UYW|1381@lyx|耒阳西|LPQ|1382@lze|临泽|LEJ|1383@lzg|龙爪沟|LZT|1384@lzh|雷州|UAQ|1385@lzh|六枝|LIW|1386@lzh|鹿寨|LIZ|1387@lzh|来舟|LZS|1388@lzh|龙镇|LZA|1389@lzh|拉鲊|LEM|1390@lzh|礼州|UZW|1391@man|明安|MAC|1392@mas|马鞍山|MAH|1393@mba|庙坝|MGW|1394@mba|毛坝|MBY|1395@mbg|毛坝关|MGY|1396@mcb|麻城北|MBN|1397@mcb|墨池坝|MOW|1398@mch|渑池|MCF|1399@mch|明城|MCL|1400@mch|马场|MCW|1401@mch|庙城|MAP|1402@mcn|渑池南|MNF|1403@mcp|茅草坪|KPM|1404@mcu|马村|MRW|1405@mdh|猛洞河|MUQ|1406@mds|磨刀石|MOB|1407@mdu|弥渡|MDF|1408@mdu|蒙渡|MUW|1409@mdz|美岱召|MDC|1410@mes|帽儿山|MRB|1411@mfs|民福寺|MFW|1412@mga|明港|MGN|1413@mga|木戛|MAW|1414@mga|马嘎|MVW|1415@mgt|马盖图|MGC|1416@mhk|梅河口|MHL|1417@mhs|梅花山|MHW|1418@mhu|马皇|MHZ|1419@mjg|孟家岗|MGB|1420@mji|麻江|BBW|1421@mjz|马架子|MBC|1422@mla|美兰|MHQ|1423@mld|汨罗东|MQQ|1424@mlh|马莲河|MHB|1425@mli|茅岭|MLZ|1426@mli|庙岭|MLL|1427@mli|穆棱|MLB|1428@mli|麻栗|MLW|1429@mli|马林|MID|1430@mlo|汨罗|MLQ|1431@mlo|马龙|MGM|1432@mlt|木里图|MUD|1433@mml|密马龙|MMM|1434@mni|冕宁|UGW|1435@mpa|沐滂|MPQ|1436@mqh|马桥河|MQB|1437@mqi|闽清|MQS|1438@mqu|民权|MQF|1439@msh|明水河|MUT|1440@msh|麻山|MAB|1441@msh|眉山|MSW|1442@msh|冕山|MQW|1443@msw|漫水湾|MKW|1444@msz|茂舍祖|MOM|1445@msz|米沙子|MST|1446@mtz|庙台子|MZB|1447@mwa|麻旺|NOW|1448@mxi|美溪|MEB|1449@mxi|孟溪|MZQ|1450@mxi|磨溪|JIW|1451@mxi|勉县|MVY|1452@mya|麻阳|MVQ|1453@myc|牧羊村|MCM|1454@myi|米易|MMW|1455@myu|麦园|MYS|1456@myu|墨玉|MUR|1457@myu|密云|MUP|1458@mzg|庙子沟|MZW|1459@mzh|庙庄|MZJ|1460@mzh|米脂|MEY|1461@mzh|木竹河|MEW|1462@mzu|民族|MZC|1463@nan|宁安|NAB|1464@nan|农安|NAT|1465@nbo|尼波|NPW|1466@nbs|南博山|NBK|1467@nch|南仇|NCK|1468@ncs|南城司|NSP|1469@ncu|宁村|NCZ|1470@nde|宁德|NES|1471@neg|南尔岗|NVW|1472@ngc|南观村|NGP|1473@ngd|南宫东|NFP|1474@ngl|南关岭|NLT|1475@ngu|宁国|NNH|1476@nha|宁海|NHH|1477@nhc|南河川|NHJ|1478@nhu|南华|NHS|1479@nhy|闹海营|NHP|1480@nji|牛家|NJB|1481@nji|宁家|NVT|1482@nji|能家|NJD|1483@njn|内江南|NXW|1484@nko|南口|NKP|1485@nkq|南口前|NKT|1486@nla|南朗|NNQ|1487@nli|乃林|NLD|1488@nlk|尼勒克|NIR|1489@nlu|那罗|ULZ|1490@nlx|宁陵县|NLF|1491@nma|奈曼|NMD|1492@nmi|宁明|NMZ|1493@nmu|南木|NMX|1494@npn|南平南|NNS|1495@npu|那铺|NPZ|1496@npz|牛坪子|NZW|1497@nqi|南桥|NQD|1498@nqu|那曲|NQO|1499@nqu|暖泉|NQJ|1500@nri|尼日|NRW|1501@nta|南台|NTT|1502@nwu|宁武|NWV|1503@nwz|南湾子|NWP|1504@nxb|南翔北|NEH|1505@nxi|宁乡|NXQ|1506@nxi|内乡|NXF|1507@nxt|牛心台|NXT|1508@nyu|南峪|NUP|1509@nzg|娘子关|NIP|1510@nzh|南召|NAF|1511@nzm|南杂木|NZT|1512@pan|平安|PAL|1513@pan|蓬安|PAW|1514@pay|平安驿|PNO|1515@paz|磐安镇|PAJ|1516@paz|平安镇|PZT|1517@pba|蒲坝|PVW|1518@pcd|蒲城东|PEY|1519@pch|蒲城|PCY|1520@pde|裴德|PDB|1521@pde|平等|PDW|1522@pdi|偏店|PRP|1523@pdm|平顶庙|PDC|1524@pdx|平顶山西|BFF|1525@pdx|坡底下|PXJ|1526@ped|普洱渡|PUW|1527@pet|瓢儿屯|PRT|1528@pfa|平房|PFB|1529@pgu|平关|PGM|1530@pgu|盘关|PAM|1531@pgu|平果|PGZ|1532@phb|徘徊北|PHP|1533@phk|平河口|PHM|1534@pjb|盘锦北|PBD|1535@pjd|潘家店|PDP|1536@pko|皮口|PKT|1537@pld|普兰店|PLT|1538@pli|偏岭|PNT|1539@psh|平山|PSB|1540@psh|彭山|PSW|1541@psh|皮山|PSR|1542@psh|彭水|PHW|1543@psh|磐石|PSL|1544@psh|平社|PSV|1545@pta|平台|PVT|1546@pti|平田|PTM|1547@pti|莆田|PTS|1548@ptq|葡萄菁|PTW|1549@pwa|平旺|PWV|1550@pxi|普雄|POW|1551@pxi|蓬溪|KZW|1552@pxi|沛县|PXI|1553@pya|平洋|PYX|1554@pya|彭阳|PYJ|1555@pya|平遥|PYV|1556@pyi|平邑|PIK|1557@pyp|平原堡|PPJ|1558@pyu|平原|PYK|1559@pyu|平峪|PYP|1560@pze|彭泽|PZG|1561@pzh|邳州|PJH|1562@pzh|平庄|PZD|1563@pzi|泡子|POD|1564@pzn|平庄南|PND|1565@pzw|堡子湾|PZC|1566@qan|乾安|QOT|1567@qan|庆安|QAB|1568@qan|迁安|QQP|1569@qdj|七甸|QDM|1570@qdo|祁东|QRQ|1571@qfd|曲阜东|QAK|1572@qfe|庆丰|QFT|1573@qft|奇峰塔|QVP|1574@qfu|曲阜|QFK|1575@qfy|勤丰营|QFM|1576@qga|青杠|QGW|1577@qha|琼海|QYQ|1578@qhe|千河|QUY|1579@qhe|清河|QIP|1580@qhm|清河门|QHD|1581@qht|齐哈日格图|QHC|1582@qhy|清华园|QHP|1583@qji|渠旧|QJZ|1584@qji|綦江|QJW|1585@qji|全椒|INH|1586@qji|秦家|QJB|1587@qjp|祁家堡|QBT|1588@qjx|清涧县|QNY|1589@qjz|秦家庄|QZV|1590@qlc|青龙场|QUW|1591@qlh|七里河|QLD|1592@qli|渠黎|QLZ|1593@qli|秦岭|QLY|1594@qls|青龙寺|QSM|1595@qls|青龙山|QGH|1596@qlx|七龙星|QLW|1597@qme|祁门|QIH|1598@qmt|前磨头|QMP|1599@qsg|清水沟|WNW|1600@qsh|青山|QSB|1601@qsh|全胜|QVB|1602@qsh|确山|QSN|1603@qsh|清水|QUJ|1604@qsm|七苏木|QSC|1605@qsy|戚墅堰|QYH|1606@qti|青田|QVH|1607@qto|桥头|QAT|1608@qtx|青铜峡|QTJ|1609@qwt|前苇塘|QWP|1610@qxi|青溪|QIW|1611@qxi|渠县|QRW|1612@qxi|淇县|QXF|1613@qxi|祁县|QXV|1614@qxi|青县|QXP|1615@qxi|桥西|QXJ|1616@qxu|清徐|QUV|1617@qxy|旗下营|QXC|1618@qya|千阳|QOY|1619@qya|祁阳|QVQ|1620@qya|沁阳|QYF|1621@qya|泉阳|QYL|1622@qyi|七营|QYJ|1623@qys|庆阳山|QSJ|1624@qyu|清远|QBQ|1625@qyu|清原|QYT|1626@qzd|钦州东|QDZ|1627@qzh|全州|QZZ|1628@qzh|钦州|QRZ|1629@qzs|青州市|QZK|1630@qzx|茄子溪|QXW|1631@ran|瑞安|RAH|1632@rch|荣昌|RCW|1633@rch|瑞昌|RCG|1634@rga|如皋|RBH|1635@rgu|容桂|RUQ|1636@rqi|任丘|RQP|1637@rsh|乳山|ROK|1638@rsh|融水|RSZ|1639@rsh|热水|RSD|1640@rxi|容县|RXZ|1641@rya|饶阳|RVP|1642@rya|汝阳|RYF|1643@ryh|绕阳河|RHD|1644@rzh|汝州|ROF|1645@sba|石坝|OBJ|1646@sba|松坝|OBW|1647@sba|沙坝|OIW|1648@sbc|上板城|SBP|1649@sbi|施秉|AQW|1650@sbn|上板城南|OBP|1651@sbs|石板哨|SRW|1652@sca|上仓|SKP|1653@sch|商城|SWN|1654@sch|莎车|SCR|1655@sch|石场|SKW|1656@sch|顺昌|SCS|1657@sch|舒城|OCH|1658@sch|神池|SMV|1659@sch|水城|OCW|1660@sch|沙城|SCP|1661@sch|石城|SCT|1662@scz|山城镇|SCL|1663@sda|山丹|SDJ|1664@sde|顺德|ORQ|1665@sde|绥德|ODY|1666@sdi|上店|ODC|1667@sdo|邵东|SOQ|1668@sdo|水洞|SIL|1669@sdu|商都|SXC|1670@sdu|十渡|SEP|1671@sdw|四道湾|OUD|1672@sdy|三道营|SDC|1673@sdz|三堆子|ODW|1674@sfa|绅坊|OLH|1675@sfe|双丰|OFB|1676@sft|四分滩|SFC|1677@sft|四方台|STB|1678@sfu|水富|OTW|1679@sfu|双福|SFW|1680@sfy|双凤驿|SYW|1681@sgk|三关口|OKJ|1682@sgl|桑根达来|OGC|1683@sgu|韶关|SNQ|1684@sgz|上高镇|SVK|1685@sha|上杭|JBS|1686@sha|沙海|SED|1687@shc|顺河场|SCW|1688@she|松河|SBM|1689@she|沙河|SHP|1690@shk|沙河口|SKT|1691@shl|赛汗塔拉|SHC|1692@shs|沙河市|VOP|1693@sht|山河屯|SHL|1694@shu|水花|SGW|1695@shx|三河县|OXP|1696@shy|四合永|OHD|1697@shz|三汇镇|OZW|1698@shz|双河镇|SEL|1699@shz|石河子|SZR|1700@shz|三合庄|SVP|1701@sjd|三家店|ODP|1702@sjh|沈家河|OJJ|1703@sjh|松江河|SJL|1704@sji|尚家|SJB|1705@sji|孙家|SUB|1706@sji|沈家|OJB|1707@sji|三江|SNW|1708@sji|苏集|SJC|1709@sji|松江|SAH|1710@sjk|三江口|SKD|1711@sjl|司家岭|OLK|1712@sjn|松江南|IMH|1713@sjn|石景山南|SRP|1714@sjt|邵家堂|SJJ|1715@sjx|三江县|SOZ|1716@sjz|施家嘴|SHM|1717@sjz|三家寨|SMM|1718@sjz|十家子|SJD|1719@sjz|松江镇|OZL|1720@sjz|三介海子|SPC|1721@sjz|深井子|SWT|1722@ska|松坎|OKW|1723@sld|什里店|OMP|1724@sle|疏勒|SUR|1725@slh|疏勒河|SHJ|1726@slh|舍力虎|VLD|1727@sli|石磷|SPB|1728@sli|石林|SLM|1729@sli|绥棱|SIB|1730@sli|石岭|SOL|1731@sli|双流|ORW|1732@sln|石林南|LNM|1733@slo|石龙|SLQ|1734@slq|萨拉齐|SLC|1735@slu|商洛|OLY|1736@slu|索伦|SNT|1737@slz|沙岭子|SLP|1738@smc|石门村|SMC|1739@smd|沙马拉达|OLW|1740@sme|思|OMW|1741@smk|石门坎|SMW|1742@smn|三门峡南|SCF|1743@smx|三门县|OQH|1744@smx|石门县|OMQ|1745@smx|三门峡西|SXF|1746@sni|肃宁|SYP|1747@son|宋|SOB|1748@spa|双牌|SBZ|1749@spi|遂平|SON|1750@spt|沙坡头|SFJ|1751@spx|上普雄|OPW|1752@sqn|商丘南|SPF|1753@squ|水泉|SID|1754@sqx|石泉县|SXY|1755@sqz|石桥子|SQT|1756@sqz|石桥镇|SZW|1757@src|石人城|SRB|1758@sre|石人|SRL|1759@srn|沙日乃|ORC|1760@ssh|山市|SQB|1761@ssh|神树|SWB|1762@ssh|鄯善|SSR|1763@ssh|三水|SJQ|1764@ssh|泗水|OSK|1765@ssh|双水|SLW|1766@ssh|树舍|OSW|1767@ssh|松树|SFT|1768@ssh|首山|SAT|1769@ssj|三十家|SRD|1770@ssp|沙沙坡|OGW|1771@ssp|三十里堡|SST|1772@ssq|双石桥|SUW|1773@ssz|松树镇|SSL|1774@sth|索图罕|SHX|1775@stj|三堂集|SDH|1776@sto|石头|OTB|1777@sto|神头|SEV|1778@stu|石沱|OWW|1779@stu|沙沱|SFM|1780@swa|沙湾|SVW|1781@swa|上万|SWP|1782@sws|赛乌苏|SWC|1783@swu|孙吴|SKB|1784@swx|沙湾县|SXR|1785@sxi|遂溪|SXZ|1786@sxi|沙县|SAS|1787@sxi|苏雄|OEW|1788@sxi|绍兴|SOH|1789@sxi|歙县|OVH|1790@sxp|上西铺|SXM|1791@sxz|石峡子|SXJ|1792@sya|绥阳|SYB|1793@sya|沭阳|FMH|1794@sya|寿阳|SYV|1795@sya|水洋|OYP|1796@syb|三元坝|OYW|1797@syc|三阳川|SYJ|1798@syd|上腰墩|SPJ|1799@syi|三营|OEJ|1800@syi|顺义|SOP|1801@syj|三义井|OYD|1802@syp|三源浦|SYL|1803@syt|三营图|QIC|1804@syu|三原|SAY|1805@syu|上虞|BDH|1806@syu|上园|SUD|1807@syu|水源|OYJ|1808@syz|桑园子|SAJ|1809@szb|石子坝|SXW|1810@szb|绥中北|SND|1811@szb|苏州北|OHH|1812@szd|宿州东|SRH|1813@szh|深州|OZP|1814@szh|孙镇|OZY|1815@szh|绥中|SZD|1816@szh|尚志|SZB|1817@szh|师庄|SNM|1818@szi|松滋|SIN|1819@szo|师宗|SEM|1820@szq|苏州园区|KAH|1821@szq|苏州新区|ITH|1822@szs|石嘴山|SZJ|1823@tan|泰安|TMK|1824@tan|台安|TID|1825@tay|通安驿|TAJ|1826@tba|桐柏|TBF|1827@tba|田坝|TAW|1828@tba|太白|TIW|1829@tbe|通北|TBB|1830@tch|汤池|TCX|1831@tch|桐城|TTH|1832@tch|郯城|TZK|1833@tch|铁厂|TCL|1834@tcu|桃村|TCK|1835@tda|通道|TRQ|1836@tdo|田东|TDZ|1837@tdq|头道桥|TDC|1838@tew|桃儿湾|TWC|1839@tga|天岗|TGL|1840@tgl|土贵乌拉|TGC|1841@tgu|太谷|TGV|1842@tgx|铜鼓溪|TPW|1843@tgy|铜罐驿|TGW|1844@tha|塔哈|THX|1845@tha|棠海|THM|1846@the|太和|TUW|1847@the|唐河|THF|1848@the|泰和|THG|1849@thu|太湖|TKH|1850@tji|团结|TIX|1851@tjj|谭家井|TNJ|1852@tjt|唐家沱|KOW|1853@tjt|陶家屯|TOT|1854@tjz|统军庄|TZP|1855@tka|泰康|TKX|1856@tka|土坎|TWW|1857@tko|铁口|TKW|1858@tld|吐列毛杜|TMD|1859@tlh|图里河|TEX|1860@tli|亭亮|TIZ|1861@tli|田林|TFZ|1862@tli|铜陵|TJH|1863@tli|铁力|TLB|1864@tme|天门|TMN|1865@tms|太姥山|TLS|1866@tmt|土牧尔台|TRC|1867@tmz|桐木寨|TMW|1868@tmz|土门子|TCJ|1869@tna|潼南|TVW|1870@tna|洮南|TVT|1871@tpc|太平川|TIT|1872@tpq|陶卜齐|TBC|1873@tpz|太平镇|TEB|1874@tqi|图强|TQX|1875@tqi|台前|TTK|1876@tql|天桥岭|TQL|1877@tqz|土桥子|TQJ|1878@tsc|汤山城|TCT|1879@tsh|桃山|TAB|1880@tsh|陶思浩|TSC|1881@tsz|塔石咀|TIM|1882@tto|滩头|TTW|1883@twh|汤旺河|THB|1884@txi|天西|TXZ|1885@txi|同心|TXJ|1886@txi|土溪|TSW|1887@txi|桐乡|TCH|1888@txi|铁西|TXW|1889@tya|田阳|TRZ|1890@tyi|桃映|TKQ|1891@tyi|天义|TND|1892@tyi|汤阴|TYF|1893@tyl|驼腰岭|TIL|1894@tys|太阳山|TYJ|1895@tyu|汤原|TYB|1896@tyy|塔崖驿|TYP|1897@tzd|滕州东|TEK|1898@tzh|台州|TZH|1899@tzh|天祝|TZJ|1900@tzh|滕州|TXK|1901@tzh|天镇|TZV|1902@tzl|桐子林|TEW|1903@tzs|天柱山|QWH|1904@wan|文安|WBP|1905@wan|武安|WAP|1906@waz|王安镇|WVP|1907@wcg|五叉沟|WCT|1908@wch|温春|WDB|1909@wch|吴场|WUW|1910@wch|王场|WCW|1911@wdc|五大连池|WRB|1912@wde|文登|WBK|1913@wdg|五道沟|WDL|1914@wdh|五道河|WHP|1915@wdi|文地|WNZ|1916@wdo|卫东|WVT|1917@wds|武当山|WRN|1918@wds|温都和硕|WDC|1919@wdu|望都|WDP|1920@weh|乌尔旗汗|WHX|1921@wfa|潍坊|WFK|1922@wft|万发屯|WFB|1923@wfu|王府|WUT|1924@wfx|五凤溪|WXW|1925@wga|王岗|WGB|1926@wgo|武功|WGY|1927@wgo|湾沟|WGL|1928@wgt|吴官田|WGM|1929@wha|乌海|WVC|1930@whb|乌海北|WBC|1931@whe|苇河|WHB|1932@whu|卫辉|WHF|1933@wjc|吴家川|WCJ|1934@wji|五家|WUB|1935@wji|威箐|WAM|1936@wji|午汲|WJP|1937@wji|弯集|WJI|1938@wke|倭肯|WQB|1939@wks|五棵树|WKT|1940@wlb|五龙背|WBT|1941@wld|乌兰哈达|WLC|1942@wle|万乐|WEB|1943@wlg|瓦拉干|WVX|1944@wlh|乌兰花|WHC|1945@wli|温岭|VHH|1946@wli|五莲|WLK|1947@wlq|乌拉特前旗|WQC|1948@wls|乌拉山|WSC|1949@wlt|卧里屯|WLX|1950@wlt|乌兰胡同|WUC|1951@wnb|渭南北|WBY|1952@wne|乌奴耳|WRX|1953@wni|万宁|WNQ|1954@wni|万年|WWG|1955@wnn|渭南南|WVY|1956@wnz|渭南镇|WNJ|1957@wpi|沃皮|WPT|1958@wpu|吴堡|WUY|1959@wqi|吴桥|WUP|1960@wqi|汪清|WQL|1961@wqi|弯|WQW|1962@wqi|武清|WWP|1963@wqu|温泉|WQM|1964@wsh|武山|WSJ|1965@wsh|文水|WEV|1966@wsz|魏善庄|WSP|1967@wto|王瞳|WTP|1968@wts|五台山|WSV|1969@wtz|王团庄|WZJ|1970@wwu|五五|WVR|1971@wxd|无锡东|WGH|1972@wxi|卫星|WVB|1973@wxi|闻喜|WXV|1974@wxi|武乡|WVV|1975@wxq|无锡新区|IFH|1976@wxu|武穴|WXN|1977@wxu|吴圩|WYZ|1978@wya|王杨|WYB|1979@wyb|瓦窑坝|WCC|1980@wyi|五营|WWB|1981@wyi|武义|RYH|1982@wyt|瓦窑田|WIM|1983@wyu|五原|WYC|1984@wzg|苇子沟|WZL|1985@wzh|韦庄|WZY|1986@wzh|五寨|WZV|1987@wzt|王兆屯|WZB|1988@wzu|瓦祖|WOW|1989@wzz|微子镇|WQP|1990@wzz|魏杖子|WKD|1991@xan|兴安|XAZ|1992@xan|新安|EAM|1993@xax|新安县|XAF|1994@xaz|新安庄|XAC|1995@xba|新保安|XAP|1996@xba|夏坝|XBW|1997@xbc|下板城|EBP|1998@xbl|西八里|XLP|1999@xca|下仓|ECP|2000@xch|宣城|ECH|2001@xch|兴城|XCD|2002@xch|新场|XOW|2003@xcu|小村|XEM|2004@xcy|新绰源|XRX|2005@xcz|下城子|XCB|2006@xde|喜德|EDW|2007@xdj|小得江|EJM|2008@xdm|西大庙|XMP|2009@xdo|小董|XEZ|2010@xdo|小东|XOD|2011@xdp|西斗铺|XPC|2012@xfe|息烽|XFW|2013@xfe|信丰|EFG|2014@xfe|襄汾|XFV|2015@xga|新干|EGG|2016@xga|小高|XGW|2017@xga|孝感|XGN|2018@xgc|西固城|XUJ|2019@xgy|夏官营|XGJ|2020@xgz|西岗子|NBB|2021@xgz|新固镇|XQP|2022@xhe|襄河|XXB|2023@xhe|新和|XIR|2024@xhe|宣和|XWJ|2025@xhe|兴和|XBC|2026@xhj|斜河涧|EEP|2027@xhq|西胡尔清|XRC|2028@xht|新华屯|XAX|2029@xhu|新华|XHB|2030@xhu|新化|EHQ|2031@xhu|宣化|XHP|2032@xhx|兴和西|XEC|2033@xhy|小河沿|XYD|2034@xhy|下花园|XYP|2035@xhz|小河镇|EKY|2036@xji|徐家|XJB|2037@xji|新绛|XJV|2038@xji|辛集|ENP|2039@xji|新江|XJM|2040@xjk|西街口|EKM|2041@xjt|许家屯|XJT|2042@xjt|许家台|XTJ|2043@xjz|谢家镇|XMT|2044@xka|兴凯|EKB|2045@xkz|下坑子|XKC|2046@xla|小榄|EAQ|2047@xla|香兰|XNB|2048@xla|星朗|ELW|2049@xld|兴隆店|XDD|2050@xle|新乐|ELP|2051@xlg|锡林呼都嘎|XLC|2052@xli|新林|XPX|2053@xli|小岭|XLB|2054@xli|新李|XLJ|2055@xli|西林|XYB|2056@xli|西里|XIC|2057@xli|西柳|GCT|2058@xli|新凉|XVW|2059@xli|仙林|XPH|2060@xlm|夏拉哈马|XMC|2061@xlt|新立屯|XLD|2062@xlx|小路溪|XLM|2063@xlz|兴隆镇|XZB|2064@xlz|新立镇|XGT|2065@xmi|新民|XMD|2066@xms|西麻山|XMB|2067@xmt|下马塘|XAT|2068@xna|孝南|XNV|2069@xnb|咸宁北|XRN|2070@xni|兴宁|ENQ|2071@xni|咸宁|XNN|2072@xny|小南垭|XNW|2073@xpb|新平坝|XMW|2074@xpi|西平|XPN|2075@xpi|兴平|XPY|2076@xpt|新坪田|XPM|2077@xpu|霞浦|XOS|2078@xpu|溆浦|EPQ|2079@xpu|犀浦|XIW|2080@xpx|下普雄|XPW|2081@xqi|新青|XQB|2082@xqi|新邱|XQD|2083@xqp|兴泉堡|XQJ|2084@xrq|仙人桥|XRL|2085@xsg|小寺沟|ESP|2086@xsh|杏树|XSB|2087@xsh|夏石|XIZ|2088@xsh|仙水|EXW|2089@xsh|浠水|XZN|2090@xsh|下社|XSV|2091@xsh|徐水|XSP|2092@xsh|小哨|XAM|2093@xsp|新松浦|XOB|2094@xst|杏树屯|XDT|2095@xsw|许三湾|XSJ|2096@xta|滩|XTW|2097@xta|邢台|XTP|2098@xtc|新铁村|XEW|2099@xtu|徐屯|XUW|2100@xtz|下台子|EIP|2101@xwe|徐闻|XJQ|2102@xwp|新窝铺|EPD|2103@xwu|修武|XWF|2104@xxg|西小沟|XXC|2105@xxi|新县|XSN|2106@xxi|息县|ENN|2107@xxi|西乡|XQY|2108@xxi|西峡|XIF|2109@xxi|孝西|XOV|2110@xxj|小新街|XXM|2111@xxx|新兴县|XGQ|2112@xxz|西小召|XZC|2113@xxz|小西庄|XXP|2114@xya|向阳|XDB|2115@xya|旬阳|XUY|2116@xya|星耀|XDC|2117@xyb|旬阳北|XBY|2118@xye|兴业|SNZ|2119@xyg|小雨谷|XHM|2120@xyi|信宜|EEQ|2121@xyj|小月旧|XFM|2122@xyq|小扬气|XYX|2123@xyu|祥云|EXM|2124@xyu|襄垣|EIF|2125@xyx|夏邑县|EJH|2126@xyy|新友谊|EYB|2127@xyz|新阳镇|XZJ|2128@xzd|徐州东|UUH|2129@xzf|新帐房|XZX|2130@xzh|悬钟|XRP|2131@xzh|新肇|XZT|2132@xzh|学庄|EZW|2133@xzh|忻州|XXV|2134@xzi|汐子|XZD|2135@xzm|西哲里木|XRD|2136@xzz|新杖子|ERP|2137@yan|依安|YAX|2138@yan|姚安|YAC|2139@yan|永安|YAS|2140@yax|永安乡|YNB|2141@ybc|渔坝村|YBM|2142@ybl|亚布力|YBB|2143@ybn|宜宾南|ANW|2144@ybs|元宝山|YUD|2145@ybt|一步滩|YIW|2146@yca|羊草|YAB|2147@ycd|秧草地|YKM|2148@ych|阳澄湖|AIH|2149@ych|迎春|YYB|2150@ych|叶城|YER|2151@ych|盐池|YKJ|2152@ych|砚川|YYY|2153@ych|阳春|YQQ|2154@ych|宜城|YIN|2155@ych|应城|YHN|2156@ych|禹城|YCK|2157@ych|晏城|YEK|2158@ych|羊场|YED|2159@ych|阳城|YNF|2160@ych|阳岔|YAL|2161@ych|俞冲|YUW|2162@ych|郓城|YPK|2163@ych|雁翅|YAP|2164@ycl|云彩岭|ACP|2165@ycx|虞城县|IXH|2166@ycz|营城子|YCT|2167@yde|永登|YDJ|2168@yde|英德|YDQ|2169@ydi|永定|YGS|2170@ydi|尹地|YDM|2171@yds|雁荡山|YGH|2172@ydu|于都|YDG|2173@ydu|园墩|YAJ|2174@yfu|永福|YFZ|2175@yfy|永丰营|YYM|2176@yga|杨岗|YRB|2177@yga|阳高|YOV|2178@ygu|阳谷|YIK|2179@yha|友好|YOB|2180@yha|余杭|EVH|2181@yhc|沿河城|YHP|2182@yhl|伊和恩格拉|YNC|2183@yhu|月华|YQW|2184@yhu|岩会|AEP|2185@yjb|盐津北|KCW|2186@yjh|羊臼河|YHM|2187@yji|永嘉|URH|2188@yji|盐津|AEW|2189@yji|余江|YHG|2190@yji|叶集|YCH|2191@yji|燕郊|AJP|2192@yji|姚家|YAT|2193@yjj|岳家井|YGJ|2194@yjp|一间堡|YJT|2195@yjs|英吉沙|YIR|2196@yjs|云居寺|AFP|2197@yjz|燕家庄|AZK|2198@yka|永康|RFH|2199@yko|垭口|YKW|2200@yla|银浪|YJX|2201@yla|永郎|YLW|2202@ylb|宜良北|YSM|2203@yld|永乐店|YDY|2204@ylh|伊拉哈|YLX|2205@yli|伊林|YLB|2206@yli|彝良|ALW|2207@yli|杨林|YLM|2208@yln|彝良南|RFW|2209@ylp|余粮堡|YLD|2210@ylq|杨柳青|YQP|2211@ylt|月亮田|YUM|2212@ylt|牙拉盖图|YTC|2213@ylw|亚龙湾|TWQ|2214@ylz|杨陵镇|YSY|2215@yma|义马|YMF|2216@yme|云梦|YMN|2217@ymh|养马河|YAW|2218@ymm|衙门庙|YMC|2219@ymo|元谋|YMM|2220@yms|一面山|YST|2221@ymz|玉门镇|YXJ|2222@yna|沂南|YNK|2223@yna|宜耐|YVM|2224@ynd|伊宁东|YNR|2225@ypi|羊坪|APW|2226@ypl|一平浪|YIM|2227@yps|营盘水|YZJ|2228@ypu|羊堡|ABM|2229@ypw|营盘湾|YPC|2230@yqb|阳泉北|YPP|2231@yqi|乐清|UPH|2232@yqi|焉耆|YSR|2233@yqi|源迁|AQK|2234@yqt|姚千户屯|YQT|2235@yqu|鱼泉|AYW|2236@yqu|阳曲|YQV|2237@ysg|榆树沟|YGP|2238@ysh|月山|YBF|2239@ysh|玉石|YSJ|2240@ysh|郁山|KSW|2241@ysh|偃师|YSF|2242@ysh|沂水|YUK|2243@ysh|榆社|YSV|2244@ysh|颍上|YVH|2245@ysh|窑上|ASP|2246@ysh|元氏|YSP|2247@ysl|杨树岭|YAD|2248@ysp|野三坡|AIP|2249@yst|榆树屯|YSX|2250@yst|榆树台|YUT|2251@ysz|鹰手营子|YIP|2252@yta|源潭|YTQ|2253@ytb|元田坝|YPW|2254@ytp|牙屯堡|YTZ|2255@yts|烟筒山|YSL|2256@ytt|烟筒屯|YUX|2257@yws|羊尾哨|YWM|2258@yxi|越西|YHW|2259@yxi|攸县|YOG|2260@yxi|油溪|YXW|2261@yxi|永修|ACG|2262@yxj|迎祥街|YJW|2263@yxu|杨漩|YYW|2264@yya|酉阳|AFW|2265@yya|余姚|YYH|2266@yyd|弋阳东|YIG|2267@yyd|岳阳东|YIQ|2268@yyi|阳邑|ARP|2269@yyu|鸭园|YYL|2270@yyz|鸳鸯镇|YYJ|2271@yzb|燕子砭|YZY|2272@yzd|宇宙地|YZC|2273@yzh|宜州|YSZ|2274@yzh|仪征|UZH|2275@yzh|兖州|YZK|2276@yzi|迤资|YQM|2277@yzu|鱼嘴|AUW|2278@yzw|羊者窝|AEM|2279@yzz|杨杖子|YZD|2280@zan|镇安|ZEY|2281@zan|治安|ZAD|2282@zba|招柏|ZBP|2283@zbw|张百湾|ZUP|2284@zch|子长|ZHY|2285@zch|枝城|ZCN|2286@zch|诸城|ZQK|2287@zch|邹城|ZIK|2288@zch|赵城|ZCV|2289@zda|章党|ZHT|2290@zdo|肇东|ZDB|2291@zfp|照福铺|ZFM|2292@zga|朱嘎|RKW|2293@zgt|章古台|ZGD|2294@zgu|赵光|ZGB|2295@zhe|中和|ZHX|2296@zhm|中华门|VNH|2297@zjc|钟家村|ZJY|2298@zjg|朱家沟|ZUB|2299@zjg|紫荆关|ZYP|2300@zji|周家|ZOB|2301@zji|诸暨|ZDH|2302@zji|郑集|ZJI|2303@zjn|镇江南|ZEH|2304@zjt|周家屯|ZOD|2305@zjt|郑家屯|ZJD|2306@zjw|褚家湾|CWJ|2307@zjx|湛江西|ZWQ|2308@zjy|朱家窑|ZUJ|2309@zjz|曾家坪子|ZBW|2310@zla|镇赉|ZLT|2311@zli|枣林|ZIV|2312@zlt|扎鲁特|ZLD|2313@zlx|扎赉诺尔西|ZXX|2314@zmt|樟木头|ZOQ|2315@zmu|中牟|ZGF|2316@znd|中宁东|ZDJ|2317@zni|中宁|VNJ|2318@znn|中宁南|ZNJ|2319@zpi|镇平|ZPF|2320@zpi|漳平|ZPS|2321@zpu|泽普|ZPR|2322@zqi|枣强|ZVP|2323@zqi|张桥|ZQY|2324@zqi|章丘|ZTK|2325@zrh|朱日和|ZRC|2326@zrl|泽润里|ZLM|2327@zsb|中山北|ZGQ|2328@zsd|樟树东|ZOG|2329@zsh|中山|ZSQ|2330@zsh|柞水|ZSY|2331@zsh|钟山|ZSZ|2332@zsh|樟树|ZSG|2333@zss|准沙日乌苏|ZSC|2334@zsz|朱石寨|ZVW|2335@ztb|昭通北|ZCW|2336@ztn|昭通南|ZFW|2337@ztz|张台子|ZZT|2338@zwo|珠窝|ZOP|2339@zwt|张维屯|ZWB|2340@zwu|彰武|ZWD|2341@zxi|棕溪|ZOY|2342@zxi|轸溪|ZQW|2343@zxi|钟祥|ZTN|2344@zxi|资溪|ZXS|2345@zxi|镇西|ZVT|2346@zxi|张辛|ZIP|2347@zxq|正镶白旗|ZXC|2348@zya|紫阳|ZVY|2349@zya|枣阳|ZYN|2350@zyb|竹园坝|ZAW|2351@zye|张掖|ZYJ|2352@zyu|镇远|ZUW|2353@zyx|朱杨溪|ZXW|2354@zzd|漳州东|GOS|2355@zzh|壮志|ZUX|2356@zzh|子洲|ZZY|2357@zzh|涿州|ZXP|2358@zzh|张寨|ZHI|2359@zzh|中寨|ZZM|2360@zzi|咋子|ZAL|2361@zzj|镇紫街|ZJW|2362@zzl|枣子林|ZEW|2363@zzs|卓资山|ZZC|2364@zzu|中嘴|EUW|2365@zzx|株洲西|ZAQ";
	private static final Map<String,String> station = new HashMap<String,String>();
	static {
		String p ="[\u4e00-\u9fa5]{2,6}\\|[A-Z]{3}";
		Pattern pattern = Pattern.compile(p);
		Matcher matcher = pattern.matcher(stationCode);
		while(matcher.find()){
			String value = matcher.group();
			String[] arr = value.split("\\|");
			if(arr.length ==2){
				station.put(arr[0], arr[1]);
			}
		}
	}
	
	public static String getStationCode(String stationName){
		return station.get(stationName);
	}
	
	@SuppressWarnings("deprecation")
	public static HttpClient getNewHttpClient(){
	    try{
	        KeyStore trustStore =KeyStore.getInstance(KeyStore.getDefaultType());

	        trustStore.load(null,null);

	        SSLSocketFactory sf =new MySSLSocketFactory(trustStore);

	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params=new BasicHttpParams();

	        HttpProtocolParams.setVersion(params,HttpVersion.HTTP_1_1);

	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry =new SchemeRegistry();

	        registry.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));

	        registry.register(new Scheme("https", sf,443));

	        ClientConnectionManager ccm =new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm,params);

	    }catch(Exception e){
	    }
	    return new DefaultHttpClient();
	}
	
	public static String mapGet(Map<String,String> params,String key,String defaultValue){
		String result = defaultValue;
		if(params == null || params.isEmpty()){
			return result;
		}
		if(params != null){
			String temp = params.get(key);
			if(temp != null && temp.trim().length() != 0){
				result = temp;
			}
		}
		return result;
	}
	
	public static Map<String,String> getParams(String fileName){
		Map<String,String> params = new HashMap<String,String>();
		Scanner sca = null;
		try {
			sca = new Scanner(new File(fileName));
			while(sca.hasNext()){
				String line = sca.next();
				if(line.startsWith("#")){
					continue;
				}
				String[] kvs = line.split("=");
				if(kvs.length ==1){
					params.put(kvs[0].trim(), "");
				} else if (kvs.length ==2){
					params.put(kvs[0].trim(), kvs[1].trim());
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			if(sca != null){
				sca.close();
			}
		}
		return params;
		
	}
	
	public static boolean isBlank(String value){
		if(null == value){
			return true;
		}
		if(value.trim().length() ==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取车次信息
	 * @param queryResult
	 * @param trainCode
	 * @return
	 */
	public static Train parseQueryResult(String queryResult,String trainCode) {
		Train train = null;
		Document doc = Jsoup.parse(queryResult);
		Elements elements = doc.select(".btn130_2");
		for (Element element : elements) {
			String value = element.attr("onclick");
			String[] values = value.split("\\'");
			train = 	new Train(values[1]);
			if(train.getTrainCode().equals(trainCode)){
				return train;
			}
		}
		return null;
	}
	
	public static OrderParam parseFileToParam(String fileName){
		Map<String,String> paramsMap = getParams(fileName);
		if(paramsMap!=null && !paramsMap.isEmpty()){
			Train train = new Train();
			String fromStationName = paramsMap.get("fromStationName");
			validField(fromStationName, "fromStationName");
			train.setFromStationName(fromStationName);
			
			String fromStationCode = getStationCode(fromStationName);
			validField(fromStationCode, "fromStationCode");
			train.setFromStationCode(fromStationCode);
			
			String toStationName =  paramsMap.get("toStationName");
			validField(toStationName, "toStationName");
			train.setToStationName(toStationName);
			
			String toStationCode = getStationCode(toStationName);
			validField(toStationCode, "toStationCode");
			train.setToStationCode(toStationCode);
			
			String trainCode = paramsMap.get("trainCode");
			validField(trainCode, "trainCode");
			train.setTrainCode(trainCode);
			
			String date = paramsMap.get("date");
			validField(date, "date");
			train.setDate(date);
			
			List<Person> persons = new ArrayList<Person>();
			for (int i = 1; i <= 5; i++) {
				String person = paramsMap.get("person_"+i);
				if(!isBlank(person)){
					String[] pas = person.split(",");
					if(pas.length==4){
						Person p = new Person();
						p.setName(pas[0]);
						p.setCardCode(pas[1]);
						p.setPhone(pas[2]);
						p.setSeatType(pas[3]);
						persons.add(p);
					}
				}
			}
			
			if(persons.isEmpty()){
				throw new IllegalArgumentException("param file content persons is empty");
			}
			return new OrderParam(train, persons);
		}
		
		return null;
	}
	
	private static void validField(String value,String fieldName){
		if(isBlank(value)){
			throw new IllegalArgumentException("param file content "+fieldName+" is empty");
		}
	}
	
	public static String getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	
	public static void main(String[] args) {
		System.out.println(getStationCode("长沙"));
	}
	
}
