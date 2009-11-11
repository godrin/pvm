def a(&block)
  block.call(123)
end

a{|x|puts x}
  
# tmp3=begin_withscope(block)
#   tmp1=123
#   tmp2=block.call(tmp1)
#   clear tmp1
#   lreturn tmp2
# end
# self.a=tmp3
# clear tmp3
# tmp7=begin(x)
#   tmp5=x
#   tmp6=puts(tmp5)
#   clear tmp5
#   clear tmp6
# end
# tmp4=a(tmp7)
# clear tmp7
  