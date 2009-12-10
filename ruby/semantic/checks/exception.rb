$i=0

def b
  $i=1
  raise "My Exception"
  $i=2
end

def a
  $i=3
  b
  $i=4
end

j=0
smoere=begin
  j=1
  a
  j=2
rescue String=>e
  puts "Got exception1"
rescue Integer,RuntimeError,VMException=>e
  puts "Got exception2"
  j=3
  if e=="My Exception"
    j=4
  elsif e.message=="My Exception"
    j=5
  else
    j=6
  end
end

puts $i
puts j

# tmp1=0
# tmp2=tmp1
# self$i=tmp2
# clear tmp1
# clear tmp2
# tmp10=begin_withscope()
#   tmp3=1
#   tmp4=tmp3
#   self$i=tmp4
#   clear tmp3
#   clear tmp4
#   tmp5="My Exception"
#   tmp6=tmp5
#   ereturn tmp6
# end
# self@b=tmp10
# OUTPUT
# 1
# 5