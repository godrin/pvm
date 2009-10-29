class IasgnCode
  def before
    []
  end

  def after
    []
  end

  def code
    @value.before+s("#{@name}=#{@value.value}")+@value.after
  end

  def value
    @name
  end
end