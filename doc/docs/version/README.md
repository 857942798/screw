# 变更记录
## SCREW各历史版本迭代记录

SCREW各历史版本分支关系及合并顺序如下图所示:    
术语： merge：表示分支最终合入的版本。   
虚线流向【end】表示该分支尚未合并。

::: warning   
【注意】：请按照版本分支关系选择对应的版本进行升级
:::

```mermaid
graph BT;
    ST((start))
    1_0(1.0)
    ED((end));

    ST-->1_0;
    1_0-->ED;

    classDef className fill:#D9D9D9,stroke-width:0;
    class ST,ED className;
    classDef default fill:#00bc7d,color:white,stroke-width:0,font-size:14px,font-family:Arial
```
