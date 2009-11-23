def a(&block)
  block.call(123)
end

a{|x|puts x}
  
# tmp4=begin_withscope(block)
#   tmp1=123
#   tmp2=tmp1
#   tmp3=block.call(tmp2)
#   clear tmp1
#   clear tmp2
#   lreturn tmp3
# end
# self@a=tmp4
# clear tmp4
# tmp8=begin(x)
#   tmp6=x
#   tmp7=puts(tmp6)
#   clear tmp6
#   lreturn tmp7
# end
# tmp5=a(tmp8)
# clear tmp8
  