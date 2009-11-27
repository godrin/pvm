def a
  raise 1
end
x=begin
  a
  puts "XX"
  "O"
rescue 
  puts "MUH"
  "HI"
end
puts x
# tmp4=begin_withscope()
#   tmp1=1
#   tmp2=tmp1
#   ereturn tmp2
# end
# self@a=tmp4
# clear tmp4
# try
#   tmp5=a()
#   clear tmp5
#   tmp6="XX"
#   tmp7=tmp6
#   tmp8=puts(tmp7)
#   clear tmp6
#   clear tmp7
#   clear tmp8
#   tmp9="O"
#   tmp15=tmp9
# rescue
#   tmp10="MUH"
#   tmp11=tmp10
#   tmp12=puts(tmp11)
#   clear tmp10
#   clear tmp11
#   clear tmp12
#   tmp13="HI"
#   tmp14=tmp13
#   tmp15=tmp14
# end
# x=tmp15
# clear tmp15
# tmp16=x
# tmp17=puts(tmp16)
# clear tmp16
# OUTPUT
# MUH
# HI