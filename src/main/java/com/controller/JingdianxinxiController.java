package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.convert.Convert;
import com.entity.YonghuEntity;
import com.service.*;
import com.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.JingdianxinxiEntity;
import com.entity.view.JingdianxinxiView;

import com.entity.StoreupEntity;

/**
 * 景点信息
 * 后端接口
 *
 * @author
 * @email
 * @date 2022-04-18 19:44:42
 */
@RestController
@RequestMapping("/jingdianxinxi")
public class JingdianxinxiController {
    @Autowired
    private JingdianxinxiService jingdianxinxiService;
    @Autowired
    private StoreupService storeupService;


    @Autowired
    private YonghuService yonghuService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, JingdianxinxiEntity jingdianxinxi,
                  HttpServletRequest request) {
        EntityWrapper<JingdianxinxiEntity> ew = new EntityWrapper<JingdianxinxiEntity>();
        PageUtils page = jingdianxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jingdianxinxi), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, JingdianxinxiEntity jingdianxinxi,
                  HttpServletRequest request) {
        EntityWrapper<JingdianxinxiEntity> ew = new EntityWrapper<JingdianxinxiEntity>();
        PageUtils page = jingdianxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jingdianxinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(JingdianxinxiEntity jingdianxinxi) {
        EntityWrapper<JingdianxinxiEntity> ew = new EntityWrapper<JingdianxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(jingdianxinxi, "jingdianxinxi"));
        return R.ok().put("data", jingdianxinxiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JingdianxinxiEntity jingdianxinxi) {
        EntityWrapper<JingdianxinxiEntity> ew = new EntityWrapper<JingdianxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(jingdianxinxi, "jingdianxinxi"));
        JingdianxinxiView jingdianxinxiView = jingdianxinxiService.selectView(ew);
        return R.ok("查询景点信息成功").put("data", jingdianxinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        JingdianxinxiEntity jingdianxinxi = jingdianxinxiService.selectById(id);
        jingdianxinxi.setClicknum(jingdianxinxi.getClicknum() + 1);
        jingdianxinxi.setClicktime(new Date());
        jingdianxinxiService.updateById(jingdianxinxi);
        return R.ok().put("data", jingdianxinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        JingdianxinxiEntity jingdianxinxi = jingdianxinxiService.selectById(id);
        jingdianxinxi.setClicknum(jingdianxinxi.getClicknum() + 1);
        jingdianxinxi.setClicktime(new Date());
        jingdianxinxiService.updateById(jingdianxinxi);
        return R.ok().put("data", jingdianxinxi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JingdianxinxiEntity jingdianxinxi, HttpServletRequest request) {
        jingdianxinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(jingdianxinxi);
        jingdianxinxiService.insert(jingdianxinxi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JingdianxinxiEntity jingdianxinxi, HttpServletRequest request) {
        jingdianxinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(jingdianxinxi);
        jingdianxinxiService.insert(jingdianxinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody JingdianxinxiEntity jingdianxinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(jingdianxinxi);
        jingdianxinxiService.updateById(jingdianxinxi);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        jingdianxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<JingdianxinxiEntity> wrapper = new EntityWrapper<JingdianxinxiEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }


        int count = jingdianxinxiService.selectCount(wrapper);
        return R.ok().put("count", count);
    }

    /**
     * 前端智能排序
     */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, JingdianxinxiEntity jingdianxinxi, HttpServletRequest request, String pre) {
        EntityWrapper<JingdianxinxiEntity> ew = new EntityWrapper<JingdianxinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StringUtils.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "clicknum");
        params.put("order", "desc");
        Map<String, Object> userParams = new LinkedHashMap<>();
        userParams.put("page", "1");
        userParams.put("limit", String.valueOf(Integer.MAX_VALUE));
        PageUtils pageUtils = yonghuService.queryPage(userParams);

        Map<String, Object> jingdianParams = new LinkedHashMap<>();
        jingdianParams.put("page", "1");
        jingdianParams.put("limit", String.valueOf(Integer.MAX_VALUE));
        PageUtils jingdianPageUtils = jingdianxinxiService.queryPage(userParams);

        Map<String, Object> storeUpParams = new LinkedHashMap<>();
        storeUpParams.put("page", "1");
        storeUpParams.put("limit", String.valueOf(Integer.MAX_VALUE));
        PageUtils storeUpPageUtils = storeupService.queryPage(storeUpParams);

        Map<Integer, YonghuEntity> userEntityMap = new LinkedHashMap<>();
        double[][] userStoreUps = new double[Convert.toInt(pageUtils.getTotal())][Convert.toInt(jingdianPageUtils.getTotal())];
        int userIndex = 0;
        for (Object user : pageUtils.getList()) {
            if (user instanceof YonghuEntity) {
                userEntityMap.put(userIndex, (YonghuEntity) user);
                userIndex++;
            }
        }

        Map<Integer, JingdianxinxiEntity> jingdianEntityMap = new LinkedHashMap<>();
        int index = 0;
        for (Object user : pageUtils.getList()) {
            double[] userStoreUp = userStoreUps[index];
            YonghuEntity userEntity = (YonghuEntity) user;
            int jingdianIndex = 0;
            for (Object jingdian : jingdianPageUtils.getList()) {
                long userId = 0;
                if (jingdian instanceof JingdianxinxiEntity) {
                    JingdianxinxiEntity jingdianxinxiEntity = (JingdianxinxiEntity) jingdian;
                    jingdianEntityMap.put(jingdianIndex, jingdianxinxiEntity);

                    for (Object storeup : storeUpPageUtils.getList()) {
                        if (storeup instanceof StoreupEntity) {
                            StoreupEntity storeupEntity = (StoreupEntity) storeup;
                            if (storeupEntity != null && jingdianxinxiEntity.getId().equals(storeupEntity.getRefid()) && userEntity.getId().equals(storeupEntity.getUserid())) {
                                userStoreUp[jingdianIndex] = 1;
                                break;
                            }
                        }
                    }
                    jingdianIndex++;
                }

            }
            if (index >= pageUtils.getList().size()) break;
            index++;
        }
        Long currentUserId = (Long) request.getSession().getAttribute("userId");
        int currentUserIndex = 0;
        for (Integer key : userEntityMap.keySet()) {
            if (userEntityMap.get(key).getId().equals(currentUserId)) {
                currentUserIndex = key;
                break;
            }
        }
        List<Integer> jingdianxinxiItems = RecommendUtils.recommendItems(currentUserIndex, userStoreUps, 5);
        List<Integer> newjingdianxinxiItems=new ArrayList<>();
        for(Integer item:jingdianxinxiItems){
            newjingdianxinxiItems.add(item-1);
        }
        List<JingdianxinxiEntity>   recommendItems=  jingdianEntityMap.keySet().stream().filter(t->newjingdianxinxiItems.contains(t)).map(t1->jingdianEntityMap.get(t1)).collect(Collectors.toList());
        PageUtils page = jingdianxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jingdianxinxi), params), params));
        page.setList(recommendItems);
        return R.ok().put("data", page);
    }


}
